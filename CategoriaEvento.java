// Enum simples para categorias de eventos
public enum CategoriaEvento {
    FESTA,
    ESPORTE,
    SHOW,
    CULTURA,
    OUTROS;  // botei no plural sem querer mas ta valendo

    public static CategoriaEvento pegar(String txt){
        try{
            return CategoriaEvento.valueOf(txt.toUpperCase());
        } catch(Exception e){
            return OUTROS;
        }
    }
}
