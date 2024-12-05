package backtracking;
import estructura.Encreuades;
import estructura.PosicioInicial;

public class SolucioBacktracking {

		private boolean [] marcatge;
		private char [][] millorSol;
		private int millorPunt;
		private  char[][] solucioActual;

	/* TODO
	 * cal definir els atributs necessaris
	 */
	private final Encreuades repte;


	public SolucioBacktracking(Encreuades repte) {
		this.repte = repte;
	}

	public char[][] getMillorSolucio() {
		return this.millorSol; //TODO
	}

	public Runnable start(boolean optim){
		marcatge= new boolean[repte.getItemsSize()];
		millorSol = null;
		millorPunt = -1;
		solucioActual = new char[repte.getPuzzle().length][];
		for (int i = 0; i < repte.getPuzzle().length; i++) {
			solucioActual[i] = repte.getPuzzle()[i].clone();
		}

		/* TODO
		 * cal inicialitzar els atributs necessaris
		 */

		if(!optim) {
			if (!this.backUnaSolucio(0))
				throw new RuntimeException("solució no trobada");
			guardarMillorSolucio();
		}else
			this.backMillorSolucio(0);
		return null;
	}

	/* esquema recursiu que troba una solució
	 * utilitzem una variable booleana (que retornem)
	 * per aturar el recorregut quan haguem trobat una solució
	 */
	private boolean backUnaSolucio(int indexUbicacio) {
		boolean trobada = false;
		// iterem sobre els possibles elements
		for(int indexItem = 0; indexItem < this.repte.getItemsSize() && !trobada; indexItem++) {
			//mirem si l'element es pot posar a la ubicació actual
			if(acceptable( indexUbicacio, indexItem)) {
				//posem l'element a la solució actual
				anotarASolucio(indexUbicacio, indexItem);

				if(esSolucio(indexUbicacio)) { // és solució si totes les ubicacions estan plenes
					return true;
				} else
					trobada = this.backUnaSolucio(indexUbicacio+1); //inserim la següent paraula
				if(!trobada)
					// esborrem la paraula actual, per després posar-la a una altra ubicació
					desanotarDeSolucio(indexUbicacio, indexItem);
			}
		}
		return trobada;
	}
	/* TODO
	 * Esquema recursiu que busca totes les solucions
	 * no cal utilitzar una variable booleana per aturar perquè busquem totes les solucions
	 * cal guardar una COPIA de la millor solució a una variable
	 */
	private void backMillorSolucio(int indexUbicacio) {
		if (indexUbicacio >= repte.getEspaisDisponibles().size()) {
			int puntuacioActual = calcularFuncioObjectiu(solucioActual);
			if (puntuacioActual > millorPunt) {
				millorPunt = puntuacioActual;
				guardarMillorSolucio();
			}
			return;
		}

		for (int indexItem = 0; indexItem < repte.getItemsSize(); indexItem++) {
			if (acceptable(indexUbicacio, indexItem)) {
				anotarASolucio(indexUbicacio, indexItem);
				backMillorSolucio(indexUbicacio + 1);
				desanotarDeSolucio(indexUbicacio, indexItem);
			}
		}
	}

	private boolean acceptable(int indexUbicacio, int indexItem) {
		if (marcatge[indexItem]) {
			return false; // no es acceptable si ja l'hem usat
		}
		PosicioInicial pos = repte.getEspaisDisponibles().get(indexUbicacio);
		char[] item = repte.getItem(indexItem);
		int fil = pos.getInitRow();
		int col = pos.getInitCol();

		for (int i = 0; i < item.length; i++) {
			if (pos.getDireccio() == 'H') {
				if (col + i >= solucioActual[0].length ||
						(solucioActual[fil][col + i] != ' ' && solucioActual[fil][col + i] != item[i])) {
					return false;
				}
			} else { // Dirección vertical
				if (fil + i >= solucioActual.length ||
						(solucioActual[fil + i][col] != ' ' && solucioActual[fil + i][col] != item[i])) {
					return false;
				}
			}
		}
		return true;
	}

	private void anotarASolucio(int indexUbicacio, int indexItem) {
		PosicioInicial pos = repte.getEspaisDisponibles().get(indexUbicacio);
		char[] item = repte.getItem(indexItem);
		int fil = pos.getInitRow();
		int col = pos.getInitCol();

		for (int i = 0; i < item.length; i++) {
			if (pos.getDireccio() == 'H') {
				solucioActual[fil][col + i] = item[i];
			} else {
				solucioActual[fil + i][col] = item[i];
			}
		}
		marcatge[indexItem] = true; // ho marca usat
	}

	private void desanotarDeSolucio(int indexUbicacio, int indexItem) {
		PosicioInicial pos = repte.getEspaisDisponibles().get(indexUbicacio);
		char[] item = repte.getItem(indexItem);
		int fil = pos.getInitRow();
		int col = pos.getInitCol();

		for (int i = 0; i < item.length; i++) {
			if (pos.getDireccio() == 'H') {
				if (solucioActual[fil][col + i] == item[i]) {
					solucioActual[fil][col + i] = ' ';
				}
			} else {
				if (solucioActual[fil + i][col] == item[i]) {
					solucioActual[fil + i][col] = ' ';
				}
			}
		}
		marcatge[indexItem] = false; // ho marca com a no usat
	}

	private boolean potElimiar(int fil, int col, char car) {
		return this.repte.getPuzzle()[fil][col]==car; //TODO
	}

	private boolean esSolucio(int index) {
		for (boolean usado : marcatge) {
			if (!usado) {
				return false;
			}
		}
		return index+1 == this.repte.getEspaisDisponibles().size();
	}

	private int calcularFuncioObjectiu(char[][] matriu) {
		int puntuacio = 0;
		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				char c = matriu[i][j];
				// Suma el valor ASCII del caràcter
				if (c != ' ' && c != '▪') {
					puntuacio += (int) c; // ASCII del carácter
				}
			}
		}
		return puntuacio;
	}

	private void guardarMillorSolucio() {
		millorSol = new char[solucioActual.length][];
		for (int i = 0; i < solucioActual.length; i++) {
			millorSol[i] = solucioActual[i].clone();
		}
	}

	public String toString() {
		StringBuilder resultat = new StringBuilder();
		for (char[] fila : solucioActual) {
			for (char c : fila) {
				resultat.append(c == ' ' ? '.' : c).append(' ');
			}
			resultat.append('\n');
		}
		return resultat.toString();
	}

}
