import java.util.NoSuchElementException;

public class Pilha<E> {

	private Celula<E> topo;
	private Celula<E> fundo;

	public Pilha() {

		Celula<E> sentinela = new Celula<E>();
		fundo = sentinela;
		topo = sentinela;

	}

	public boolean vazia() {
		return fundo == topo;
	}

	public void empilhar(E item) {

		topo = new Celula<E>(item, topo);
	}

	public E desempilhar() {

		E desempilhado = consultarTopo();
		topo = topo.getProximo();
		return desempilhado;

	}

	public E consultarTopo() {

		if (vazia()) {
			throw new NoSuchElementException("Nao há nenhum item na pilha!");
		}

		return topo.getItem();

	}

	/**
	 * Cria e devolve uma nova pilha contendo os primeiros numItens elementos
	 * do topo da pilha atual.
	 * 
	 * Os elementos são mantidos na mesma ordem em que estavam na pilha original.
	 * Caso a pilha atual possua menos elementos do que o valor especificado,
	 * uma exceção será lançada.
	 *
	 * @param numItens o número de itens a serem copiados da pilha original.
	 * @return uma nova instância de Pilha<E> contendo os numItens primeiros elementos.
	 * @throws IllegalArgumentException se a pilha não contém numItens elementos.
	 */
	public Pilha<E> subPilha(int numItens) {
		
		// TODO
		return null;
	}

	public void listarTodosOsItensPilha(){
		System.out.println("\nItens da Pilha:");
		if (this.vazia()) {
			System.out.println("Pilha vazia!");
			return;
		}
		Pilha <E> aux = new Pilha<>();
		aux = this;
		Celula<E> elemento = new Celula<E>(aux.consultarTopo());
		int i = 0;
		while (elemento.getItem() != fundo) {
			i++;
			System.out.println( i + ". "+ elemento.getItem());
			elemento = elemento.getProximo();
		}
    }

	public boolean PesquisarItemNaPilha(E e){
		Pilha <E> aux = new Pilha<>();
		boolean response = false;
		while (!this.vazia()) {
			aux.empilhar(this.desempilhar());
		}
		while (!aux.vazia()) {
			E elemento = aux.desempilhar();
			if(elemento.equals(e)){
				response = true;
			}
			this.empilhar(elemento);
		}
		return response;
    }

	public void removerItemNaPilha(E e){
		Pilha <E> aux = new Pilha<>();
		boolean response = false;
		while (!this.vazia()) {
			aux.empilhar(this.desempilhar());
		}
		while (!aux.vazia()) {
			E elemento = aux.desempilhar();
			if(elemento.equals(e)){
				continue;
			}
			this.empilhar(elemento);
		}
    }
}