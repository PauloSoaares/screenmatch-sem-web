package br.com.paulo.alura.screenmatch.screenmatch.principal;

import br.com.paulo.alura.screenmatch.screenmatch.service.ConsumoApi;

import java.util.Scanner;

public class Principal {

    private ConsumoApi consumo = new ConsumoApi();

    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=15c1824c";

    public void exibirMenu(){
        System.out.println("digite o nome da serie para pesquisar:");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    }
}
