package net.yzwlab.daap;

import java.io.IOException;

/**
 * ���C�u�����ɃA�N�Z�X���邽�߂̃Z�b�V�����ł��B
 */
public interface LibrarySession {

	/**
	 * �Z�b�V��������܂��B
	 * 
	 * @throws IOException
	 *             ���o�͊֌W�̃G���[�B
	 */
	public void close() throws IOException;

	/**
	 * ���ׂẴg���b�N�̐������擾���܂��B
	 * 
	 * @return ���ׂẴg���b�N�̐����B
	 * @throws IOException
	 *             ���o�͊֌W�̃G���[�B
	 */
	public Iterable<TrackDescription> getAllTracks() throws IOException;

}
