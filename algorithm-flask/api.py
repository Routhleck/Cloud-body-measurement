from flask import Flask, jsonify, request
from pullUp import pull_up_video_stream
from pushUp import push_up_video_stream
from sitUp import sit_up_video_stream
from squat import squat_video_stream

app = Flask(__name__)


@app.route('/pullUp', methods=['POST'])
def pull_up():
    data = request.get_json()
    flv_code = data['flv_code']
    video_stream_url = 'http://39.106.13.47:8080/live/' + flv_code + '.live.flv'
    duration = data['duration']
    pull_up_count = pull_up_video_stream(video_stream_url, duration)
    return jsonify({'pull_up_count': pull_up_count})


@app.route('/pushUp', methods=['POST'])
def push_up():
    data = request.get_json()
    flv_code = data['flv_code']
    video_stream_url = 'http://39.106.13.47:8080/live/' + flv_code + '.live.flv'
    duration = data['duration']
    push_up_count = push_up_video_stream(video_stream_url, duration)
    return jsonify({'push_up_count': push_up_count})


@app.route('/sitUp', methods=['POST'])
def sit_up():
    data = request.get_json()
    flv_code = data['flv_code']
    video_stream_url = 'http://39.106.13.47:8080/live/' + flv_code + '.live.flv'
    duration = data['duration']
    sit_up_count = sit_up_video_stream(video_stream_url, duration)
    return jsonify({'sit_up_count': sit_up_count})


@app.route('/squat', methods=['POST'])
def squat():
    data = request.get_json()
    flv_code = data['flv_code']
    video_stream_url = 'http://39.106.13.47:8080/live/' + flv_code + '.live.flv'
    duration = data['duration']
    squat_count = squat_video_stream(video_stream_url, duration)
    return jsonify({'squat_count': squat_count})


if __name__ == '__main__':
    app.run()
