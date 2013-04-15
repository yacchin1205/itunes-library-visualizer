package net.yzwlab.daap.test;

import net.yzwlab.daap.StatusListener;
import net.yzwlab.daap.StatusUpdate;

import org.tunesremote.daap.Session;
import org.tunesremote.daap.Status;

/**
 * �e�X�g�p�̃n���h���̎����ł��B
 */
public class TestStatusHandler implements StatusListener {

	/**
	 * ���݂̍Đ���Ԃ��Ǘ����܂��B
	 */
	private Status status;

	/**
	 * �\�z���܂��B
	 * 
	 * @param session
	 *            ���݂̃Z�b�V�����Bnull�͕s�B
	 */
	public TestStatusHandler(Session session) {
		if (session == null) {
			throw new IllegalArgumentException();
		}
		this.status = new Status(session, this);
	}

	@Override
	public void statusUpdated(Status source, StatusUpdate updated) {
		if (updated != StatusUpdate.UPDATE_PROGRESS) {
			return;
		}
		int progress = status.getProgress();
		int playing = status.getPlayStatus();
		String playingText = "paused";
		if (playing == Status.STATE_PLAYING) {
			playingText = "playing";
		}
		if (progress % 20 != 0) {
			return;
		}
		System.out.println("Progress(" + playingText + "): name="
				+ status.getTrackName() + " / " + status.getTrackArtist()
				+ ": " + progress + "sec. /" + status.getProgressTotal()
				+ "sec.");
	}

}
