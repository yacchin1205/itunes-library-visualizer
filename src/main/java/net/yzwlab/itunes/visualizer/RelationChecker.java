package net.yzwlab.itunes.visualizer;

/**
 * 関係のチェック処理を表現するインタフェースです。
 * 
 * @param <T>
 *            要素のタイプ。
 */
public interface RelationChecker<T> {

	/**
	 * タイプを定義します。
	 */
	public enum Type {
		NONE, FROM, TO, BOTH
	}

	/**
	 * 関係をチェックします。
	 * 
	 * @param elem1
	 *            要素1。nullは不可。
	 * @param elem2
	 *            要素2。nullは不可。
	 * @return 関係。
	 */
	public Type checkRelation(T elem1, T elem2);

}
