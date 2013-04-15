package net.yzwlab.daap;

import java.io.IOException;

/**
 * ライブラリにアクセスするためのセッションです。
 */
public interface LibrarySession {

	/**
	 * セッションを閉じます。
	 * 
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public void close() throws IOException;

	/**
	 * すべてのトラックの説明を取得します。
	 * 
	 * @return すべてのトラックの説明。
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public Iterable<TrackDescription> getAllTracks() throws IOException;

}
