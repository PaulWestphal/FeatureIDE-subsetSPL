/**
 * TODO description
 */
public class Test {
	static void addEdgesFromExisitingNodes(Graph g) {
		for (int j = 0; j < 10; j++) {
			g.addEdge(g.getNodes().get(RANDOM.nextInt(MAX)),
					g.getNodes().get(RANDOM.nextInt(MAX)));
		}
	}

	static void addEdgesNewNodes(Graph g) {
		for (int j = 0; j < 5; j++) {
			g.addEdge(new Node("NODE-" + RANDOM.nextInt(MAX * 2)), new Node(
					"NODE-" + RANDOM.nextInt(MAX * 2)));
		}
	}
}