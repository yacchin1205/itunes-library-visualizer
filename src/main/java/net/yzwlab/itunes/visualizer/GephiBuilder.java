package net.yzwlab.itunes.visualizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.ContainerFactory;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;

public class GephiBuilder {

	/**
	 * エントリを保持します。
	 */
	private Set<SongEntry> entries;

	/**
	 * 関係性チェッカーを保持します。
	 */
	private RelationChecker<String> relationChecker;

	/**
	 * 構築します。
	 */
	public GephiBuilder() {
		this.entries = new HashSet<SongEntry>();
		this.relationChecker = null;
	}

	/**
	 * 関係性チェッカを取得します。
	 * 
	 * @return 関係性チェッカ。
	 */
	public RelationChecker<String> getRelationChecker() {
		return relationChecker;
	}

	/**
	 * 関係性チェッカを設定します。
	 * 
	 * @param relationChecker
	 *            関係性チェッカ。
	 */
	public void setRelationChecker(RelationChecker<String> relationChecker) {
		this.relationChecker = relationChecker;
	}

	/**
	 * 追加します。
	 * 
	 * @param albumId
	 *            アルバムID。
	 * @param artistId
	 *            アーティストID。
	 * @param name
	 *            名前。
	 */
	public void addSong(String albumId, String artistId, String name) {
		this.entries.add(new SongEntry(albumId, artistId, name));
	}

	/**
	 * 出力します。
	 * 
	 * @param targetFile
	 *            対象ファイル。nullは不可。
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public void output(File targetFile) throws IOException {
		if (targetFile == null) {
			throw new IllegalArgumentException();
		}
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();

		Lookup.getDefault().lookup(ContainerFactory.class).newContainer();
		int index = 0;
		Map<String, Node> albumNodes = new HashMap<String, Node>();
		Map<String, Node> artistNodes = new HashMap<String, Node>();

		Graph graph = graphModel.getGraph();
		List<Node> songNodes = new ArrayList<Node>();
		for (SongEntry entry : entries) {
			Node songNode = null;
			Node albumNode = null;
			Node artistNode = null;
			if (entry.name != null) {
				Node node = graphModel.factory().newNode("song" + index);
				node.getAttributes().setValue("label", entry.name);
				graph.addNode(node);
				songNodes.add(node);
				songNode = node;
			}
			if (entry.albumId != null) {
				String id = entry.albumId;
				Node node = albumNodes.get(id);
				if (node == null) {
					node = graphModel.factory().newNode("song-album-" + index);
					node.getAttributes().setValue("label", id);
					albumNodes.put(id, node);
					graph.addNode(node);
				}
				albumNode = node;
			}
			if (entry.artistId != null) {
				String id = entry.artistId;
				Node node = artistNodes.get(id);
				if (node == null) {
					node = graphModel.factory().newNode("song-artist-" + index);
					node.getAttributes().setValue("label", id);
					artistNodes.put(id, node);
					graph.addNode(node);
				}
				artistNode = node;
			}
			if (songNode != null && albumNode != null) {
				Edge edge = graphModel.factory().newEdge(albumNode, songNode);
				edge.setWeight(1.0f);
				edge.getAttributes().setValue("label", "album-song");
				graph.addEdge(edge);
			}
			if (songNode != null && artistNode != null) {
				Edge edge = graphModel.factory().newEdge(artistNode, songNode);
				edge.setWeight(1.0f);
				edge.getAttributes().setValue("label", "artist-song");
				graph.addEdge(edge);
			}

			index++;
		}
		checkRelations(graphModel, artistNodes.values());

		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");
		exporter.setExportVisible(true);
		ec.exportFile(targetFile, exporter);
	}

	/**
	 * 関係性をチェックします。
	 * 
	 * @param graphModel
	 *            モデル。nullは不可。
	 * @param nodes
	 *            ノード。nullは不可。
	 */
	private void checkRelations(GraphModel graphModel, Iterable<Node> nodes) {
		if (graphModel == null || nodes == null) {
			throw new IllegalArgumentException();
		}
		if (relationChecker == null) {
			return;
		}

		GraphFactory graphFactory = graphModel.factory();
		for (Node node1 : nodes) {
			Object label1 = node1.getAttributes().getValue("label");
			if (label1 == null) {
				continue;
			}
			for (Node node2 : nodes) {
				Object label2 = node2.getAttributes().getValue("label");
				if (label2 == null) {
					continue;
				}

				RelationChecker.Type type = relationChecker.checkRelation(
						label1.toString(), label2.toString());
				if (type == RelationChecker.Type.FROM
						|| type == RelationChecker.Type.BOTH) {
					Edge edge = graphFactory.newEdge(node2, node1);
					edge.setWeight(1.0f);
					graphModel.getGraph().addEdge(edge);
				}
				if (type == RelationChecker.Type.TO
						|| type == RelationChecker.Type.BOTH) {
					Edge edge = graphFactory.newEdge(node1, node2);
					edge.setWeight(1.0f);
					graphModel.getGraph().addEdge(edge);
				}
			}
		}
	}

	private class SongEntry {

		private String albumId;

		private String artistId;

		private String name;

		public SongEntry(String albumId, String artistId, String name) {
			this.albumId = albumId;
			this.artistId = artistId;
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((albumId == null) ? 0 : albumId.hashCode());
			result = prime * result
					+ ((artistId == null) ? 0 : artistId.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SongEntry other = (SongEntry) obj;
			if (albumId == null) {
				if (other.albumId != null)
					return false;
			} else if (!albumId.equals(other.albumId))
				return false;
			if (artistId == null) {
				if (other.artistId != null)
					return false;
			} else if (!artistId.equals(other.artistId))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

}
