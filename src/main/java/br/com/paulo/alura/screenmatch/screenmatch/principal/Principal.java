package br.com.paulo.alura.screenmatch.screenmatch.principal;

import br.com.paulo.alura.screenmatch.screenmatch.model.DadosEpisodio;
import br.com.paulo.alura.screenmatch.screenmatch.model.DadosSerie;
import br.com.paulo.alura.screenmatch.screenmatch.model.DadosTemporada;
import br.com.paulo.alura.screenmatch.screenmatch.model.Episodio;
import br.com.paulo.alura.screenmatch.screenmatch.service.ConsumoApi;
import br.com.paulo.alura.screenmatch.screenmatch.service.ConverteDados;
import com.sun.tools.javac.Main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    //OBJETO RESPONSÁVEL PELA REQUISIÇÃO
    private ConsumoApi consumo = new ConsumoApi();
    //OBJETO RESPONSAVEL POR CONVERTER O JSON EM UMA OBJETO JAVA
    private ConverteDados conversor = new ConverteDados();

    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=15c1824c";

    public static boolean ehPrimo (int numero) {
        if (numero < 2) return false;
        for (int i = 2; i <= Math.sqrt(numero); i++){
            if (numero % i == 0) {
                return false;
            }
        }
        return false;
    }


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

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 5 episodios");


        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partire de que ano você deseja ver os episódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e-> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada " + e.getTemporada() +
                                "Episodio " + e.getTitulo() +
                                "Data " + e.getDataLancamento().format(formatador)
                ));



    }

}

