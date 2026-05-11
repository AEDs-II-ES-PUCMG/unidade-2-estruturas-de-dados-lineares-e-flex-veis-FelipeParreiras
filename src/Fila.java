import java.util.NoSuchElementException;

public class Fila<E> {

	private Celula<E> frente;
	private Celula<E> tras;

	public Fila() {
		Celula<E> sentinela = new Celula<>();
		frente = sentinela;
		tras = sentinela;
	}

	public boolean vazia() {
		return frente == tras;
	}

	public void enfileirar(E item) {
		tras.setProximo(new Celula<>(item));
		tras = tras.getProximo();
	}

	public E desenfileirar() {
		E item = consultarFrente();
		frente = frente.getProximo();
		return item;
	}

	public E consultarFrente() {
		if (vazia()) {
			throw new NoSuchElementException("Nao ha nenhum item na fila!");
		}

		return frente.getProximo().getItem();
	}

	public void listarTodosOsItensFila() {
		System.out.println("\nItens da Fila:");
		if (vazia()) {
			System.out.println("Fila vazia!");
			return;
		}

		Celula<E> atual = frente.getProximo();
		int i = 1;
		while (atual != null) {
			System.out.println(i + ". " + atual.getItem());
			atual = atual.getProximo();
			i++;
		}
	}

	@Override
	public String toString() {
		StringBuilder texto = new StringBuilder();
		Celula<E> atual = frente.getProximo();

		while (atual != null) {
			texto.append(atual.getItem()).append(System.lineSeparator());
			atual = atual.getProximo();
			if (atual != null) {
				texto.append(System.lineSeparator());
			}
		}

		return texto.toString();
	}
}
