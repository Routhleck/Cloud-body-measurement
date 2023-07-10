from flask import Flask, jsonify
from pullUp import pull_up_video_stream
from pushUp import push_up_video_stream

app = Flask(__name__)


@app.route('/pullUp')
def pull_up():
    video_stream_url = 'http://39.106.13.47:8080/live/112.live.flv'
    pull_up_count = pull_up_video_stream(video_stream_url)
    return jsonify({'pull_up_count': pull_up_count})


@app.route('/pushUp')
def push_up():
    video_stream_url = 'http://39.106.13.47:8080/live/111.live.flv'
    push_up_count = push_up_video_stream(video_stream_url)
    return jsonify({'push_up_count': push_up_count})


@app.route('/sitUp')
def sit_up():
    video_stream_url = 'http://39.106.13.47:8080/live/111.live.flv'
    push_up_count = push_up_video_stream(video_stream_url)
    return jsonify({'push_up_count': push_up_count})


@app.route('/squat')
def squat():
    video_stream_url = 'http://39.106.13.47:8080/live/111.live.flv'
    push_up_count = push_up_video_stream(video_stream_url)
    return jsonify({'push_up_count': push_up_count})


if __name__ == '__main__':
    app.run()
