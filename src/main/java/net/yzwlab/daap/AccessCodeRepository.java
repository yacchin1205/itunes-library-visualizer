package net.yzwlab.daap;

/**
 * アクセスコードのリポジトリです。
 */
public interface AccessCodeRepository extends Iterable<AccessCode> {

	/**
	 * アクセスコードを更新します。
	 * 
	 * @param accessCode
	 *            アクセスコード。nullは不可。
	 */
	public void updateCode(AccessCode accessCode);

}
