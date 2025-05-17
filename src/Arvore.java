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

    private void split(No no, boolean isFolha){
        if(isFolha){
            int qtdItensSplitFolha = getQtdItensSplitFolha();
        }
        else{
            int qtdItensSplitIntermediario = getQtdItensSplitIntermediario();
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
            folha.remanejar(pos);
            folha.setvInfo(pos, info);
            folha.setTL(folha.getTL() + 1);
            if(folha.temQueFazerSplit()){
                split(folha, true);
            }
        }
    }

    private int getQtdItensSplitFolha(){
        return (int) (Math.ceil((double) Arvore.ordem / 2));
    }

    private int getQtdItensSplitIntermediario(){
        return (int) (Math.ceil((double) Arvore.ordem / 2) - 1);
    }
}
