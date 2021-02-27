package tsp.lib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Graph<T> {

	private double[][] weights;
	private boolean[][] edges;
	private T[] nodes;
	private int size;
	private double[] D;
	private T[] P;
	private double[][] A;
	private T[][] PFloyd;
	private boolean ejecutadoFloyd;
	private String caminoMinimo;
	private String recorridoProfundidad;

	@SuppressWarnings("unchecked")
	public Graph(int dimension) {
		this.weights = new double[dimension][dimension];
		this.edges = new boolean[dimension][dimension];
		this.nodes = (T[]) new Object[dimension];
		this.size = 0;
		this.A = new double[dimension][dimension];
		this.PFloyd = (T[][]) new Object[dimension][dimension];
		this.ejecutadoFloyd = false;
	}

	public List<T> getNodes() {
		List<T> nodes = new ArrayList<T>();
		for (int i=0; i<this.size; i++)
			nodes.add(this.nodes[i]);
		return nodes;
	}
	
	public int addNode(T node) {
		/**
		 * Método que añade un nuevo nodo al grafo. Falla si el nodo ya existe en el
		 * grafo o no cabe
		 * 
		 * @param node el nodo que se desea añadir
		 * @return int 0 si el nodo es añadido correctamente; -1 si existe el nodo; -2
		 *         si no hay sitio, -3 si el nodo a insertar es nulo
		 */

		if (node == null)
			return -3;

		if (this.existNode(node))
			// Ya existe el nodo
			return -1;
		if (size >= this.nodes.length)
			// Grafo lleno
			return -2;

		this.ejecutadoFloyd = false;

		for (int i = 0; i < size; i++) {
			weights[i][size] = 0;
			weights[size][i] = 0;
			edges[i][size] = false;
			edges[size][i] = false;
		}
		this.nodes[size] = node;
		this.size++;
		return 0;
	}

	public int getNode(T node) {
		/**
		 * Método que devuelve la posición de un nodo en el vector nodes
		 * 
		 * @param node el nodo a buscar
		 * @return int la posición del nodo a buscar, devuelve -1 en caso de que no se
		 *         encuentre o el nodo que se le pase es null
		 */

		for (int i = 0; i < this.size; i++)
			if (nodes[i].equals(node))
				return i;
		// No se ha encontrado
		return -1;
	}

	public int removeNode(T node) {
		/**
		 * Método que elimina un nodo del grafo
		 *
		 * @param node el nodo a eliminar
		 * @return int 0 si ha sido posible eliminarlo; -1 en caso contrario
		 */
		int pos = getNode(node);
		if (pos == -1)
			// No existe el nodo a eliminar
			return -1;

		this.ejecutadoFloyd = false;

		this.size--;
		for (int i = 0; i < this.size; i++) {
			this.weights[pos][i] = this.weights[this.size][i];
			this.weights[i][pos] = this.weights[i][this.size];
			this.edges[pos][i] = this.edges[this.size][i];
			this.edges[i][pos] = this.edges[i][this.size];
		}
		this.edges[pos][pos] = this.edges[size][size];
		this.weights[pos][pos] = this.weights[size][size];
		this.nodes[pos] = this.nodes[size];
		return 0;
	}

	public boolean existNode(T node) {
		/**
		 * Método que comprueba si el nodo pasado como parámetro existe en grafo
		 *
		 * @param node el nodo a comprobar
		 * @return boolean true si existe el nodo
		 */
		return this.getNode(node) != -1;
	}

	public double getEdge(T source, T target) {
		/**
		 * /** Método que devuelve el peso de la arista entre dos nodos. @ return -1 no
		 * existe nodo origen, -2 no existe nodo destino, -3 no existen ninguno, -4 no
		 * existe la arista.
		 *
		 * @param source el nodo fuente
		 * @param target el nodo destino
		 * @return double el peso de la arista
		 */
		if (!existNode(source) && !existNode(target))
			return -3;
		if (!existNode(source))
			return -1;
		if (!existNode(target))
			return -2;

		if (existEdge(source, target))
			return this.weights[getNode(source)][getNode(target)];
		return -4;
	}

	public boolean existEdge(T source, T target) {
		/**
		 * Método que comprueba si existe arista entre dos nodos
		 *
		 * @param source el nodo origen
		 * @param target el nodo destino
		 * @return boolean true si existe arista
		 */
		try {
			return edges[getNode(source)][getNode(target)] || edges[getNode(target)][getNode(source)];
		} catch (Exception e) {
			return false;
		}
	}

	public int addEdge(T source, T target, double weight) {
		/**
		 * Método que añade una arista entre dos nodos
		 *
		 * @param source el nodo origen
		 * @param target el nodo destino
		 * @param weight el peso de la arista que une al nodo origen y al nodo destino
		 * @return int 0 si se ha añadido la arista; @ return -1 no existe nodo origen,
		 *         -2 no existe nodo destino, @ -3 no existen ninguno, -4 ya existe la
		 *         arista, -5 peso negativo
		 */
		if (weight < 0)
			return -5;
		if (!existNode(source) && !existNode(target))
			return -3;
		if (!existNode(source))
			return -1;
		if (!existNode(target))
			return -2;
		if (existEdge(source, target))
			return -4;

		this.ejecutadoFloyd = false;

		edges[getNode(source)][getNode(target)] = true;
		edges[getNode(target)][getNode(source)] = true;
		this.weights[getNode(source)][getNode(target)] = weight;
		this.weights[getNode(target)][getNode(source)] = weight;
		return 0;
	}

	public int removeEdge(T source, T target) {
		/**
		 * Método que elimina una arista del grafo en caso de que esta exista
		 *
		 * @param source el nodo origen
		 * @param target el nodo destino
		 * @return int 0 si ha sido posible borrar la arista; @ return -1 no existe nodo
		 *         origen, -2 no existe nodo destino, @ -3 no existen ninguno, -4 no
		 *         existe la arista
		 */
		if (!existNode(source) && !existNode(target))
			return -3;
		if (!existNode(source))
			return -1;
		if (!existNode(target))
			return -2;
		if (!existEdge(source, target))
			return -4;

		this.ejecutadoFloyd = false;

		edges[getNode(source)][getNode(target)] = false;
		this.weights[getNode(source)][getNode(target)] = 0;
		return 0;
	}

	public double[] dijkstraIterativo(T source) {
		/**
		 * Método que implementa Dijkstra Iterativo
		 * 
		 * @param boolean[] el vector S de aristas
		 * @param double[] el vector D de pesos
		 * @param T[] el vector P de nodos
		 * @return Vector D de pesos
		 */

		if (source == null || getNode(source) == -1)
			return null;

		initializeDijkstraD(source);
		initializeDijkstraP(source);

		// Conjunto s de nodos ya visitados
		boolean[] s = new boolean[this.size];
		s[getNode(source)] = true;
		int pivote;
		while ((pivote = this.getPivote(this.D, s)) != -1) {
			s[pivote] = true;
			for (int m = 0; m < this.size; m++) {
				if (!s[m] && edges[pivote][m] && D[pivote] + this.weights[pivote][m] < D[m]) {
					// Si el peso utilizando el nodo pivote a m es menor que el directo desde el
					// origen hasta m,
					// actualizamos el nuevo peso minimo y establecemos el nodo pivote en P como
					// trayecto hasta m
					D[m] = D[pivote] + this.weights[pivote][m];
					P[m] = this.nodes[pivote];
				}
			}
		}

		return this.D;
	}

	private int getPivote(double[] d, boolean[] s) {
		int posMenor = -1;
		double pesoMenor = Double.POSITIVE_INFINITY;
		for (int i = 0; i < this.size; i++) {
			if (!s[i] && d[i] < pesoMenor) {
				pesoMenor = d[i];
				posMenor = i;
			}
		}
		return posMenor;
	}

	private double[] initializeDijkstraD(T node) {
		/**
		 * Método auxiliar que genera un vector D inicializado para el Algoritmo de
		 * Dijkstra
		 *
		 * @param node Nodo para el que se desea inicializar el vector D
		 * @return double[] Vector de pesos
		 */
		this.D = new double[this.size];
		int posOr = getNode(node);
		for (int i = 0; i < D.length; i++)
			if (edges[posOr][i])
				this.D[i] = weights[posOr][i];
			else {
				this.D[i] = Double.POSITIVE_INFINITY;
				if (posOr == i)
					this.D[i] = 0;
			}
		return D;
	}

	@SuppressWarnings("unchecked")
	private T[] initializeDijkstraP(T node) {
		/**
		 * Método auxiliar que genera un vector P inicializado para el Algoritmo de
		 * Dijkstra
		 *
		 * @param node Nodo para el que se desea inicializar el vector
		 * @return T[] Vector de caminos
		 */
		this.P = (T[]) new Object[this.size];
		int posOr = getNode(node);
		for (int i = 0; i < P.length; i++)
			if (edges[posOr][i])
				this.P[i] = this.nodes[posOr];
			else
				this.P[i] = null;
		P[posOr] = this.nodes[posOr];
		return P;
	}

	public int floyd() {
		/**
		 * Aplica el algoritmo de Floyd al grafo y devuelve 0 si lo aplica y genera
		 * matrices A y P; y –1 si no lo hace
		 */
		if (this.size == 0 || this.nodes == null)
			return -1;
		this.ejecutadoFloyd = true;
		this.initializeFloydA();
		this.initializeFloydP();

		for (int k = 0; k < this.size; k++) {
			for (int i = 0; i < this.size; i++) {
				for (int j = 0; j < this.size; j++) {
					if (this.A[i][j] > this.A[i][k] + this.A[k][j]) {
						this.A[i][j] = this.A[i][k] + this.A[k][j];
						this.PFloyd[i][j] = this.nodes[k];
					}
				}
			}
		}

		return 0;
	}

	protected double[][] getAFloyd() {
		/**
		 * Devuelve la matriz A de Floyd Si no se ha invocado a Floyd debe devolver null
		 */
		if (this.A == null)
			return null;
		return this.A;
	}

	protected T[][] getPFloyd() {
		/**
		 * Devuelve la matriz P de Floyd, Si no se ha invocado a Floyd debe devolver
		 * null
		 */
		if (this.PFloyd == null)
			return null;
		return this.PFloyd;
	}

	private T[][] initializeFloydP() {
		/**
		 * Método que inicializa la matriz de caminos P para el Algoritmo de Floyd
		 *
		 * @return T[][] la matriz de caminos / return int[][] la matriz de caminos
		 */
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (edges[i][j])
					this.PFloyd[i][j] = this.nodes[i];
				else
					this.PFloyd[i][j] = null;
			}
		}

		return this.PFloyd;
	}

	private double[][] initializeFloydA() {
		/**
		 * Método que inicializa la matriz de pesos A para el Algoritmo de Floyd
		 *
		 * @return double[][] la matriz de pesos
		 */
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (edges[i][j])
					this.A[i][j] = this.weights[i][j];
				else {
					this.A[i][j] = Double.POSITIVE_INFINITY;
					if (j == i)
						this.A[i][j] = 0;
				}
			}
		}

		return this.A;
	}

	public double minCostPath(T origen, T destino) {
		/**
		 * Devuelve el coste del camino de coste mínimo entre origen y destino según
		 * Floyd. Si no están generadas las matrices de Floyd, las genera. Si no puede
		 * obtener el valor por alguna razón, devuelve –1 (OJO que es distinto de
		 * infinito)
		 **/
		int posOr = getNode(origen);
		int posDes = getNode(destino);
		if (posOr == -1 || posDes == -1)
			return -1;
		if (!this.ejecutadoFloyd)
			this.floyd();
		return this.A[posOr][posDes];
	}

	public String path(T origen, T destino) {
		/**
		 * Indica el camino entre los nodos que se le pasan como parámetros de esta
		 * forma:
		 *
		 * Origen<tab>(coste0)<tab>Intermedio1<tab>(coste1)...IntermedioN<tab>(costeN)
		 * Destino Si no hay camino: Origen<tab>(Infinity)<tab>Destino Si Origen y
		 * Destino coinciden: Origen Si no existen los 2 nodos devuelve una cadena vacía
		 *
		 * @param origen
		 * @param destino
		 * @return El String con lo indicado
		 */
		String res = "";
		int posOr = getNode(origen);
		int posDes = getNode(destino);

		if (posOr == -1 || posDes == -1)
			return res; // Cadena vacía

		if (origen.equals(destino)) {
			res = origen.toString();
			return res; // Solo Origen
		}

		if (this.minCostPath(origen, destino) == Double.POSITIVE_INFINITY) {
			res = origen.toString() + "\t(Infinity)\t" + destino.toString();
			return res; // No hay camino
		}

		caminoMinimo = destino.toString();
		pathFloydIntermedios(origen, destino);
		return this.caminoMinimo;

	}

	private void pathFloydIntermedios(T origen, T destino) {
		/**
		 * Método auxiliar que muestra el camino mínimo entre 2 nodos usando las
		 * matrices de Floyd y devuelve el camino en un string global creado para
		 * devolver el camino de Floyd
		 *
		 * @return void
		 */
		T intermedio = this.PFloyd[getNode(origen)][getNode(destino)];
		caminoMinimo = intermedio.toString() + "\t(" + weights[getNode(intermedio)][getNode(destino)] + ")\t"
				+ caminoMinimo;
		if (origen.equals(intermedio)) {
			return;
		}
		pathFloydIntermedios(origen, intermedio);

	}

	public String recorridoProfundidad(T nodo) {
		/**
		 * Lanza el recorrido en profundidad de un grafo desde un nodo determinado, No
		 * necesariamente recorre todos los nodos. Al recorrer cada nodo añade el
		 * toString del nodo y un tabulador Se puede usar un método privado recursivo...
		 * Si no existe el nodo devuelve una cadena vacia
		 */
		if (getNode(nodo) == -1)
			return "";
		this.recorridoProfundidad = "";
		boolean[] visited = new boolean[this.size];
		recursivoProfundidad(nodo, visited);
		return this.recorridoProfundidad;
	}

	private void recursivoProfundidad(T source, boolean[] visited) {
		/**
		 * Método auxiliar que implementa el Recorrido en Profundidad: Desde un nodo
		 * pasado como parámetro, actualiza el vector de nodos visitados con dicho nodo
		 * y comprueba si existe camino para el resto de nodos que no se encuentren
		 * visitados. En caso de que exista camino, repite el mismo proceso para el
		 * nuevo nodo.
		 *
		 * Además, imprime el recorrido seguido en cadenaDFS
		 *
		 * @param source  el nodo origen
		 * @param visited el vector de nodos visitados
		 */
		visited[getNode(source)] = true;
		recorridoProfundidad = recorridoProfundidad + nodes[getNode(source)].toString();
		for (T w : this.nodes) {
			if (existEdge(source, w) && !visited[getNode(w)]) {
				recorridoProfundidad = recorridoProfundidad + "\t";
				recursivoProfundidad(w, visited);
			}
		}

	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##");
		String cadena = "";
		cadena += "VECTOR NODOS\n";
		for (int i = 0; i < size; i++) {
			cadena += nodes[i].toString() + "\t";
		}
		cadena += "\n\nMATRIZ ARISTAS\n";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (edges[i][j])
					cadena += "T\t";
				else
					cadena += "F\t";
			}
			cadena += "\n";
		}
		cadena += "\nMATRIZ PESOS\n";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cadena += (edges[i][j] ? df.format(weights[i][j]) : "-") + "\t";
			}
			cadena += "\n";
		}

		double[][] aFloyd = getAFloyd();
		if (aFloyd != null) {
			cadena += "\nAFloyd\n";
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					cadena += df.format(aFloyd[i][j]) + "\t";
				}
				cadena += "\n";
			}
		}
		T[][] pFloyd = getPFloyd();
		if (pFloyd != null) {
			cadena += "\nPFloyd\n";
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (pFloyd[i][j] != null) // Modificars si pFloyd[i][j]!=-1
						cadena += pFloyd[i][j].toString() + "-" + "\t";
				}
				cadena += "\n";
			}
		}

		return cadena;
	}

}
