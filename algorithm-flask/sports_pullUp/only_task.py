from datetime import datetime
import os
from apscheduler.schedulers.blocking import BlockingScheduler

import videoprocess as vp


def test_tick():
    
    repetitions_count, out_video_path = vp.video_process()
    print("count: {}, output file: {}".format(repetitions_count, out_video_path))
    print('The time is: %s' % datetime.now())

if __name__ == '__main__':
    scheduler = BlockingScheduler()
    scheduler.add_job(test_tick, 'interval', seconds=5)

    try:
        scheduler.start()
    except (KeyboardInterrupt, SystemExit):
        pass