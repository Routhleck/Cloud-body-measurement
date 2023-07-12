
import json
from requests import session
from flask import Flask, jsonify, request
from aliOSS import UPLOAD_FILE, BASE_URL
import uuid
import time
import videoprocess as vp

from flask_sqlalchemy import SQLAlchemy


############################################################
app = Flask(__name__)
#####################################################
#--------------数据库部分
#####################################################
app.config['SECRET_KEY'] = '123456'
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://cloud_sports:CJvgV7mfG76Rcu7FNoGA@rm-wz9yxho9eg3x33hpono.mysql.rds.aliyuncs.com:3306/cloud_sports'
# 协议：mysql+pymysql
# 用户名：cloud_sports
# 密码：CJvgV7mfG76Rcu7FNoGA
# IP地址：rm-wz9yxho9eg3x33hpono.mysql.rds.aliyuncs.com
# 端口：3306
# 数据库名：cloud_sports #这里的数据库需要提前建好
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN']=True
db=SQLAlchemy(app)


# 新建表task
class Task(db.Model):
    __tablename__='task_dev'
    id = db.Column(db.Integer, primary_key=True) # 任务号
    video_type = db.Column(db.String(64)) # 运动类型
    token = db.Column(db.String(64)) # token
    video_url = db.Column(db.String(1024)) # 原视频链接
    processed_video_url = db.Column(db.String(1024)) # 处理后视频链接
    points_data = db.Column(db.String(65535)) # 点数据
    angle_data = db.Column(db.String(65535)) # 角度数据
    number = db.Column(db.String(64)) # 个数
    state = db.Column(db.String(64)) # 当前处理状态 init processing done
    

    def __repr__(self):
        return '<Task {} {}>'.format(self.id, self.video_url)

#####################################################
#--------------数据库部分
#####################################################

@app.route('/addVideoToQueue', methods=['GET', 'POST'])
def addVideoToQueue():
    """提交视频到队列"""
    if not request.headers.get('cs-token'):
        return jsonify({
            "error": 10000,
            "message": "缺少token",
        })
    
    if request.method == 'POST':
        post_json = json.loads(request.get_data())
        try:
            video_file = post_json['video']
            video_type = post_json['type']
        except Exception as e:
            return jsonify({
                "error": 10001,
                "message": "参数缺失",
            })
    else:
        try:
            video_file = request.args.get("video")
            video_type = request.args.get("type")
        except Exception as e:
            return jsonify({
                "error": 10001,
                "message": "参数缺失",
            })
    token = request.headers.get('cs-token')
    # 保存到队列
    t1 = Task(token=token,
        video_url=video_file,
        video_type=video_type,
        state="init")
    db.session.add_all([t1])
    db.session.commit()
    

    return jsonify({
        "task_id": t1.id
    })


@app.route('/getVideoProcessResult', methods=['GET', 'POST'])
def getVideoProcessResult():
    """查询视频处理结果"""
    if not request.headers.get('cs-token'):
        return jsonify({
            "error": 10000,
            "message": "缺少token",
        })
    
    if request.method == 'POST':
        post_json = json.loads(request.get_data())
        task_id = post_json['task_id']
        if not task_id:
            return jsonify({
                "error": 10001,
                "message": "参数缺失",
            })
    else:
        try:
            task_id = request.args.get("task_id")
        except Exception as e:
            return jsonify({
                "error": 10001,
                "message": "参数缺失",
            })

    # 保存到队列
    task = Task.query.get(task_id)

    print(task)
    if not task:
        return jsonify({
            "error": 10003,
            "message": "找不到任务",
        })

    if task.state == 'done':
        return jsonify({
            "task_id": task.id,
            "video_url": task.video_url,
            "processed_video_url": task.processed_video_url,
            "video_type": task.video_type,
            "number": task.number,
            "state": task.state,
            "token": task.token
        })
    else:
        return jsonify({
            "task_id": task.id,
            "state": task.state,
            "token": task.token
        })


if __name__ == '__main__':

    
    # 初始化定时任务
    # from models.taskScheduler import scheduler

    from flask_apscheduler import APScheduler
    scheduler = APScheduler()
    # interval example, 间隔执行, 每1秒执行一次
    @scheduler.task('interval', id='do_job_pullUp', max_instances=1, seconds=1, misfire_grace_time=900)
    def job_pullUp():
        
        with app.app_context():  # Create an :class:`~flask.ctx.AppContext`.
            tasks = Task.query.filter(Task.state == 'init', Task.video_type == 'pullUp').first()
            if tasks:
                tasks.state = 'processing'
                db.session.commit()
                print(tasks)
                
                repetitions_count, out_video_path = vp.video_process(tasks.video_url)
                print("count: {}, output file: {}".format(repetitions_count, out_video_path))

                # 将视频上传
                upload_class = UPLOAD_FILE(subfilename="cloudSports/video")#初始化oss
                video = open(out_video_path, 'rb')
                name = str(uuid.uuid4()).replace('-', '') + str(int(time.time()))
                video_name = '{}01.mp4'.format(name)
                processedVideoBucketUrl = upload_class.upload_file(type='content', content_name=video_name, content_file=video)
                processedVideoUrl = '{}/{}'.format(BASE_URL, processedVideoBucketUrl)

                tasks.processed_video_url = processedVideoUrl
                tasks.number = repetitions_count

                print(tasks)
                print(processedVideoUrl)
                print(repetitions_count)

                tasks.state = 'done'
                db.session.commit()

    scheduler.init_app(app)
    scheduler.start()


    app.run(host='0.0.0.0', port=5001)

