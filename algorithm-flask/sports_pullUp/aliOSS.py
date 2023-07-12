# 定义oss类
import requests
import oss2
import os

BASE_URL = 'http://typora-maidong.oss-cn-beijing.aliyuncs.com'
class UPLOAD_FILE():
    def __init__(self,subfilename,key="LTAI4FyMDEYPHEuyPRLh7Kw1",password="9W2JRwiW9bFIShBnpGixfzLze6kj5T"):
        auth = oss2.Auth(key,password) # 初始化
        # http://typora-maidong.oss-cn-beijing.aliyuncs.com/cloudSports/video/03c256e9f24046ecb3923cc2082d3ba.avi
        self.bucket = oss2.Bucket(auth, "http://oss-cn-beijing.aliyuncs.com", "typora-maidong")
        self.subfilename = subfilename# oss 路径# oss 路径
        print(self.subfilename)
    def upload_file(self,path_list=None,path=None,type='content',content_file=None,content_name=None):
        if path_list:
            list_file = os.listdir(path_list)
            for file in list_file:
                local_file = path_list + "\\" + "{}".format(file)
                osspath = self.subfilename + '/' + file
                print(osspath)
                self.up_file(osspath, local_file)
        elif path:
            osspath = self.subfilename + '/' +path.split('/')[-1]
            local_file = path
            self.up_file(osspath,local_file)
            return osspath
        elif type=='content':
            osspath = self.subfilename + '/' + content_name
            exist = self.bucket.object_exists(osspath)
            if exist:
                print("oss have files with the same name, ignore oss upload")
                return osspath
            else:
                self.bucket.put_object(osspath, content_file)
                print(" {} 上传成功".format(osspath))
                return osspath
        else:
            print("未指定路径")

    def up_file(self,osspath,local_file):
        # 先检测oss上是否有该文件
        exist = self.bucket.object_exists(osspath)
        if exist:
            print("oss have files with the same name, ignore oss upload")
        else:
            # 上传文件
            with open(local_file, "rb") as fileobj:
                result1 = self.bucket.put_object(osspath, fileobj)
                print("{} 上传成功".format(osspath))
            if int(result1.status) != 200:
                print("oss upload faild %s" % osspath)
# 下载数据，操作oss类
def oss2_data(oss_class,datasurl,name):
    if datasurl:
        try:
            print(datasurl)
            res = requests.get(datasurl, timeout=5,verify=False)
            content_name = name
            content_file = res.content
            osspath = oss_class.upload_file(type='content', content_name=content_name, content_file=content_file)
            return osspath
        except Exception as e:
            print(e)
            return None

if __name__ == "__main__":
    upload_class = UPLOAD_FILE(subfilename="cloudSports/image")#初始化oss,创建mypic/exam目录

    oss2_data(upload_class,'http://typora-maidong.oss-cn-beijing.aliyuncs.com/1615711650_3025.jpg','test.jpg')#下载www.eaam.jpg ,上传oss