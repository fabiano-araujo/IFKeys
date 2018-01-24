package br.com.fabiano.snapdark.ifkeys.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import br.com.fabiano.snapdark.ifkeys.Fragments.AddProduto;
import br.com.fabiano.snapdark.ifkeys.Fragments.ReservaFrag;
import br.com.fabiano.snapdark.ifkeys.Fragments.SalaFrag;
import br.com.fabiano.snapdark.ifkeys.Fragments.SearchFrag;
import br.com.fabiano.snapdark.ifkeys.model.Reserva;
import br.com.fabiano.snapdark.ifkeys.model.Sala;
import br.com.fabiano.snapdark.ifkeys.model.Produto;


/**
 * Created by Fabiano on 23/07/2017.
 */

public class FragControl {
    public static SearchFrag getSearchFrag(){
        SearchFrag searchFrag = new SearchFrag();
        return searchFrag;
    }
    public static Fragment getADDProdutoFrag(int idLoja) {
        AddProduto addProduto = new AddProduto();
        Bundle bundle = new Bundle();
        bundle.putInt("idLoja",idLoja);
        addProduto.setArguments(bundle);
        return addProduto;
    }
    public static Fragment getProdutoFrag(Produto produto) {
        /*ProdutoFrag produtoFrag = new ProdutoFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelable("produto",produto);
        produtoFrag.setArguments(bundle);
        return produtoFrag;*/
        return null;
    }
    public static Fragment getReservaFrag(Reserva reserva) {
        ReservaFrag reservaFrag = new ReservaFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelable("reserva", reserva);
        reservaFrag.setArguments(bundle);
        return reservaFrag;
    }

    public static Fragment getSalaFrag(Sala sala) {
        SalaFrag lojaFrag = new SalaFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelable("sala", sala);
        lojaFrag.setArguments(bundle);
        return lojaFrag;
    }
}
