from flask import Flask, render_template, Response, request
import cv2
from flask_cors import *

app = Flask(__name__, static_folder='./static')

CORS(app, supports_credentials=True)


class VideoCamera(object):
    def __init__(self):
        self.video = None

    def start_capture(self):
        if self.video is None:
            self.video = cv2.VideoCapture(0)

    def stop_capture(self):
        if self.video is not None:
            self.video.release()
            self.video = None

    def get_frame(self):
        success, image = self.video.read()
        cv2.putText(image, "hello", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 255, 0))
        ret, jpeg = cv2.imencode('.jpg', image)
        return jpeg.tobytes()


def gen(camera):
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')


@app.route('/')
@cross_origin(supports_credentials=True)
def index():
    return render_template('index.html')


@app.route('/your-backend-endpoint', methods=['POST'])
@cross_origin(supports_credentials=True)
def your_backend_endpoint():
    command = request.json.get('command')

    if command == 'open capture':
        camera.start_capture()
        print('摄像头已开启')
    elif command == 'stop capture':
        camera.stop_capture()
        print('摄像头已关闭')

    return 'OK'


@app.route('/video_feed')
def video_feed():
    return Response(gen(camera), mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    camera = VideoCamera()
    app.run(host='0.0.0.0', debug=True, port=5000)
