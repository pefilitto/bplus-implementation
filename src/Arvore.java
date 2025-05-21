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
        while(no.getvLig(0) != null)
        {
            pos = no.procurarPosicao(info);
            no = no.getvLig(pos);
        }
        return no;
    }

    private No buscarPai(No no){
        No atual = raiz, pai = null;

        while(atual.getvLig(0) != null){
            pai = atual;
            int pos = atual.procurarPosicao(no.getvInfo(0));
            atual = atual.getvLig(pos);
        }

        return pai;
    }

    private void split(No no, No pai) {
        No cx1, cx2;

        if (no instanceof NoFolha) {
            int qtd = getQtdItensSplitFolha();
            cx1 = new NoFolha();
            cx2 = new NoFolha();

            for (int i = 0; i < qtd; i++) {
                cx1.setvInfo(i, no.getvInfo(i));
            }

            for (int i = qtd; i < no.getTL(); i++) {
                cx2.setvInfo(i - qtd, no.getvInfo(i));
            }

            if(no != raiz){
                if(((NoFolha) no).getAnt() != null){
                    ((NoFolha) no).getAnt().setProx((NoFolha) cx1);
                }
                else {
                    ((NoFolha) cx1).setAnt(null);
                }
                ((NoFolha) cx1).setAnt(((NoFolha) no).getAnt());

                if(((NoFolha) no).getProx() != null){
                    ((NoFolha) no).getProx().setAnt((NoFolha) cx2);
                }
                else {
                    ((NoFolha) cx2).setProx(null);
                }
                ((NoFolha) cx2).setProx(((NoFolha) no).getProx());
            }

            ((NoFolha) cx1).setProx((NoFolha) cx2);
            ((NoFolha) cx2).setAnt((NoFolha) cx1);

            cx1.setTL(qtd);
            cx2.setTL(no.getTL() - qtd);
        }
        else {
            int qtd = getQtdItensSplitIntermediario();
            cx1 = new NoIntermediario();
            cx2 = new NoIntermediario();

            int i;
            for (i = 0; i < qtd; i++) {
                cx1.setvInfo(i, no.getvInfo(i));
                cx1.setvLig(i, no.getvLig(i));
                cx1.setvLig(i + 1, no.getvLig(i + 1));
            }

            for (int j = i; j < no.getTL(); j++) {
                cx2.setvInfo(j, no.getvInfo(j));
                cx2.setvLig(j, no.getvLig(j));
                cx2.setvLig(j + 1, no.getvLig(j + 1));
            }

            cx1.setTL(qtd);
            cx2.setTL(no.getTL() - qtd);

            cx2.remanejarTirandoPrimeiroItem(false); //Sobrescreve no primeiro elemento do vInfo
        }

        if (no == pai || pai == null) {
            NoIntermediario novoPai = new NoIntermediario();
            novoPai.remanejar(0);
            novoPai.setvInfo(0, cx2.getvInfo(0));
            novoPai.setvLig(0, cx1);
            novoPai.setvLig(1, cx2);
            novoPai.setTL(1);
            raiz = novoPai;

            if(no instanceof NoIntermediario){
                cx2.remanejarTirandoPrimeiroItem(true);
            }
        }
        else {
            int keyPromovida = cx2.getvInfo(0);
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

    public void inserir(int info){
        if(raiz == null){
            raiz = new NoFolha();
            raiz.setvInfo(0, info);
            raiz.setvLig(0, null);
            raiz.setvLig(1, null);
            raiz.setTL(1);
        }
        else{
            No folha = navegarAteFolha(info);
            int pos = folha.procurarPosicao(info);
            if(folha.getTL() < Arvore.ordem){
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
