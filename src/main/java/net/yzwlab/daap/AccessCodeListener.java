package net.yzwlab.daap;

/**
 * ライブラリアクセスのリスナです。
 */
public interface AccessCodeListener {

	/**
	 * エラーが発見されたことを通知します。
	 * 
	 * @param service
	 *            サービス。nullは不可。
	 * @param error
	 *            エラー。nullは不可。
	 */
	public void errorDetected(LibraryService service, Throwable error);

	/**
	 * iTunesが見つかったことを通知します。
	 * 
	 * @param service
	 *            サービス。nullは不可。
	 * @param accessCode
	 *            アクセスコード。nullは不可。
	 */
	public void itunesFound(LibraryService service, AccessCode accessCode);

	/**
	 * アクセスコードが見つかったことを通知します。
	 * 
	 * @param service
	 *            サービス。nullは不可。
	 * @param accessCode
	 *            アクセスコード。nullは不可。
	 */
	public void accessCodeFound(LibraryService service,
			AccessCode accessCode);

}
