package br.com.paulo.alura.screenmatch.screenmatch.principal;

import br.com.paulo.alura.screenmatch.screenmatch.model.DadosEpisodio;
import br.com.paulo.alura.screenmatch.screenmatch.model.DadosSerie;
import br.com.paulo.alura.screenmatch.screenmatch.model.DadosTemporada;
import br.com.paulo.alura.screenmatch.screenmatch.service.ConsumoApi;
import br.com.paulo.alura.screenmatch.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=15c1824c";

    public void exibirMenu(){
        System.out.println("digite o nome da serie para pesquisar:");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i<=dados.totalTemporadas(); i++){
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
