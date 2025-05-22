public class Arvore {
    private No raiz;
    public static int ordem;

    public Arvore(int ordem) {
        Arvore.ordem = ordem;
        this.raiz = null;
    }

    private No navegarAteFolha(int info)
    {
        No no = raiz;
        int pos;
        while(no.getvLig(0) != null){
            pos = no.procurarPosicao(info);
            no = no.getvLig(pos);
        }
        return no;
    }

    private No buscarPai(No no) {
        if (no == raiz) return null;

        return buscarPaiRecursivo(raiz, no);
    }

    private No buscarPaiRecursivo(No atual, No procurado) {
        if (atual == null || atual instanceof NoFolha) {
            return null;
        }

        for (int i = 0; i <= atual.getTL(); i++) {
            if (atual.getvLig(i) == procurado) {
                return atual;
            }
        }

        for (int i = 0; i <= atual.getTL(); i++) {
            No resultado = buscarPaiRecursivo(atual.getvLig(i), procurado);
            if (resultado != null) {
                return resultado;
            }
        }

        return null;
    }

    private void split(No no, No pai) {
        No cx1, cx2;
        int keyPromovida;

        if (no instanceof NoFolha) {
            int qtd = getQtdItensSplitFolha();
            cx1 = new NoFolha();
            cx2 = new NoFolha();

            for (int i = 0; i < qtd; i++) {
                cx1.setvInfo(i, no.getvInfo(i));
            }
            cx1.setTL(qtd);

            for (int i = qtd; i < no.getTL(); i++) {
                cx2.setvInfo(i - qtd, no.getvInfo(i));
            }
            cx2.setTL(no.getTL() - qtd);

            if (no != raiz) {
                ((NoFolha) cx1).setAnt(((NoFolha) no).getAnt());
                if (((NoFolha) no).getAnt() != null) {
                    ((NoFolha) no).getAnt().setProx((NoFolha) cx1);
                }

                ((NoFolha) cx2).setProx(((NoFolha) no).getProx());
                if (((NoFolha) no).getProx() != null) {
                    ((NoFolha) no).getProx().setAnt((NoFolha) cx2);
                }
            }

            ((NoFolha) cx1).setProx((NoFolha) cx2);
            ((NoFolha) cx2).setAnt((NoFolha) cx1);

            keyPromovida = cx2.getvInfo(0);
        }
        else {
            int qtd = getQtdItensSplitIntermediario();
            cx1 = new NoIntermediario();
            cx2 = new NoIntermediario();

            for (int i = 0; i < qtd; i++) {
                cx1.setvInfo(i, no.getvInfo(i));
                cx1.setvLig(i, no.getvLig(i));
            }
            cx1.setvLig(qtd, no.getvLig(qtd));
            cx1.setTL(qtd);

            keyPromovida = no.getvInfo(qtd);

            for (int i = qtd + 1; i < no.getTL(); i++) {
                cx2.setvInfo(i - (qtd + 1), no.getvInfo(i));
                cx2.setvLig(i - (qtd + 1), no.getvLig(i));
            }
            cx2.setvLig(no.getTL() - (qtd + 1), no.getvLig(no.getTL()));
            cx2.setTL(no.getTL() - (qtd + 1));
        }

        if (no == raiz || no == pai) {
            NoIntermediario novaRaiz = new NoIntermediario();
            novaRaiz.setvInfo(0, keyPromovida);
            novaRaiz.setvLig(0, cx1);
            novaRaiz.setvLig(1, cx2);
            novaRaiz.setTL(1);
            raiz = novaRaiz;
        }
        else {
            int pos = pai.procurarPosicao(keyPromovida);
            pai.remanejar(pos);
            pai.setvInfo(pos, keyPromovida);
            pai.setvLig(pos, cx1);
            pai.setvLig(pos + 1, cx2);
            pai.setTL(pai.getTL() + 1);

            if (pai.temQueFazerSplit()) {
                split(pai, buscarPai(pai));
            }
        }
    }

    public void inserir(int info) {
        if (raiz == null) {
            raiz = new NoFolha();
            raiz.setvInfo(0, info);
            raiz.setvLig(0, null);
            raiz.setvLig(1, null);
            raiz.setTL(1);
        }
        else {
            No folha = navegarAteFolha(info);
            int pos = folha.procurarPosicao(info);
            if (folha.getTL() < Arvore.ordem) {
                folha.remanejar(pos);
                folha.setvInfo(pos, info);
                folha.setTL(folha.getTL() + 1);
            }
            if(folha.temQueFazerSplit()){
                split(folha, buscarPai(folha));
            }
        }
    }

    private int getQtdItensSplitFolha(){
        return (int) (Math.ceil((double) Arvore.ordem / 2));
    }

    private int getQtdItensSplitIntermediario(){
        return (int) (Math.ceil((double) Arvore.ordem / 2) - 1);
    }

    public void in_ordem () {
        No no = raiz;
        while(!(no instanceof NoFolha)){
            no = no.getvLig(0);
        }

        while(no != null){
            for(int i=0; i < no.getTL(); i++){
                System.out.println(no.getvInfo(i));
            }
            no = ((NoFolha) no).getProx();
        }
    }
}
