package backtracking;
import estructura.Encreuades;
import estructura.PosicioInicial;

public class SolucioBacktracking {

	/* TODO
	 * cal definir els atributs necessaris
	 */
	private final Encreuades repte;

	
	public SolucioBacktracking(Encreuades repte) {
		this.repte = repte;
	}

	public char[][] getMillorSolucio() {
		return null; //TODO
	}

	public Runnable start(boolean optim){
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

	}
	
	private boolean acceptable(int indexUbicacio, int indexItem) {
		PosicioInicial pos= this.repte.getEspaisDisponibles().get(indexUbicacio);
		char[] item = this.repte.getItem(indexItem);
		int fil = pos.getInitRow();
		int col = pos.getInitCol();
		int length = pos.getLength();
		char direccio = pos.getDireccio();
		for(int i = 0; i < item.length; i++) {
			if(direccio == 'H') {
				if(col+i >= this.repte.getPuzzle()[0].length || this.repte.getPuzzle()[fil][col+i] != ' ' && this.repte.getPuzzle()[fil][col+i] != item[i])
					return false;
			}else {
				if(fil+i >= this.repte.getPuzzle().length || this.repte.getPuzzle()[fil+i][col] != ' ' && this.repte.getPuzzle()[fil+i][col] != item[i])
					return false;
			}

		}

		return true; 
	}
	
	private void anotarASolucio(int indexUbicacio, int indexItem) {
		PosicioInicial pos= this.repte.getEspaisDisponibles().get(indexUbicacio);
		//Obtenim la paraula
		char[] item = this.repte.getItem(indexItem);
		int fil = pos.getInitRow(); //creem les variables perque el codi sigui mes facil de llegir.
		int col = pos.getInitCol();
		for(int i = 0; i < item.length; i++) {
			//horitzontal
			if(pos.getDireccio() == 'H') {
				this.repte.getPuzzle()[fil][col+i] = item[i];
			}else {
				this.repte.getPuzzle()[fil+i][col] = item[i];
			}
		}
	}
	
	private void desanotarDeSolucio(int indexUbicacio, int indexItem) {
		PosicioInicial pos= this.repte.getEspaisDisponibles().get(indexUbicacio);
		//Obtenim la paraula
		char[] item = this.repte.getItem(indexItem);
		int fil = pos.getInitRow(); //creem les variables perque el codi sigui mes facil de llegir.
		int col = pos.getInitCol();
		for(int i = 0; i < item.length+1; i++) {
			//horitzontal
			if(pos.getDireccio()== 'H') {
				if (potElimiar(fil, col+i, item[i])) {
					this.repte.getPuzzle()[fil][col+i] = ' ';
				}
			}else{
				if (potElimiar(fil+i, col, item[i])) {
					this.repte.getPuzzle()[fil+i][col] = ' ';
				}
			}
		}
	}

	private boolean potElimiar(int fil, int col, char car) {
		return this.repte.getPuzzle()[fil][col]==car; //TODO
	}
	
	private boolean esSolucio(int index) {
		for (char[] row : this.repte.getPuzzle()) {
			for (char c : row) {
				if (c == ' ')
					return false;
			}
		}
		return true;
	}
	
	private int calcularFuncioObjectiu(char[][] matriu) {
		return 0; //TODO
	}
	
	private void guardarMillorSolucio() {
		// TODO - cal guardar un clone
	}
	
	public String toString() {
		String resultat = "";
		//TODO
		return resultat;
	}

}
