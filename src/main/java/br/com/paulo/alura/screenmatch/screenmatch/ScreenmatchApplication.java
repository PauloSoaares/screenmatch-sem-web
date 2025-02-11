package br.com.paulo.alura.screenmatch.screenmatch;

import br.com.paulo.alura.screenmatch.screenmatch.model.DadosEpisodio;
import br.com.paulo.alura.screenmatch.screenmatch.model.DadosSerie;
import br.com.paulo.alura.screenmatch.screenmatch.model.DadosTemporada;
import br.com.paulo.alura.screenmatch.screenmatch.service.ConsumoApi;
import br.com.paulo.alura.screenmatch.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=game+of+thrones&apikey=15c1824c");
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
		json = consumoApi.obterDados("http://www.omdbapi.com/?t=game+of+thrones&season=1&episode=2&apikey=15c1824c");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i<=dados.totalTemporadas(); i++){
			json = consumoApi.obterDados("http://www.omdbapi.com/?t=game+of+thrones&season=" + i + "&apikey=15c1824c");
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);
	}
}
