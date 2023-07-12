from flask import Flask, jsonify, request
from pullUp import pull_up_video_stream
from pushUp import push_up_video_stream
from sitUp import sit_up_video_stream
from squat import squat_video_stream

app = Flask(__name__)


@app.route('/exercise', methods=['POST'])
def exercise():
    data = request.get_json()
    exercise_type = data['actionName']
    flv_code = data['streamCode']
    video_stream_url = 'http://39.106.13.47:8080/live/' + flv_code + '.live.flv'
    duration = data['limitTime']

    if exercise_type == 'pullUp':
        count = pull_up_video_stream(video_stream_url, duration)
        return jsonify({'pull_up_count': count})

    elif exercise_type == 'pushUp':
        count = push_up_video_stream(video_stream_url, duration)
        return jsonify({'push_up_count': count})

    elif exercise_type == 'sitUp':
        count = sit_up_video_stream(video_stream_url, duration)
        return jsonify({'sit_up_count': count})

    elif exercise_type == 'squat':
        count = squat_video_stream(video_stream_url, duration)
        return jsonify({'squat_count': count})

    else:
        return jsonify({'error': 'Invalid exercise type.'}), 400


if __name__ == '__main__':
    app.config['JSONIFY_TIMEOUT'] = 60  # 设置超时时间为60秒
    app.run(threaded=True, port=5000, host='0.0.0.0', debug=True)
