import java.util.Comparator;

public class CriterioDeBuscaPorDescricao implements Comparator<ItemDePedido> {

    @Override
    public int compare(ItemDePedido item1, ItemDePedido item2) {
        String descricao1 = item1.getProduto().descricao;
        String descricao2= item2.getProduto().descricao;
        if(descricao1.equalsIgnoreCase(descricao2)){
            return 1;
        }else{
            return 0;
        }
    }
}
