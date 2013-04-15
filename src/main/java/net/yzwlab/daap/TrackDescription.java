package net.yzwlab.daap;

/**
 * トラックの情報を定義します。
 */
public interface TrackDescription {

	/**
	 * 名前を取得します。
	 * 
	 * @return 名前。
	 */
	public String getName();

	/**
	 * アーティストIDを取得します。
	 * 
	 * @return アーティストID。
	 */
	public String getArtistId();

	/**
	 * アルバムIDを取得します。
	 * 
	 * @return アルバムID。
	 */
	public String getAlbumId();

}
