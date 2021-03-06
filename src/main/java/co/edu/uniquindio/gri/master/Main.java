package co.edu.uniquindio.gri.master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.gri.dao.GrupoDAO;
import co.edu.uniquindio.gri.dao.IdiomasDAO;
import co.edu.uniquindio.gri.dao.InvestigadorDAO;
import co.edu.uniquindio.gri.dao.LineasInvestigacionDAO;
import co.edu.uniquindio.gri.model.Grupo;
import co.edu.uniquindio.gri.utils.Constantes;

@Component
public class Main implements CommandLineRunner {

	@Autowired
	GrupoDAO grupoDAO;

	@Autowired
	InvestigadorDAO investigadorDAO;

	@Autowired
	LineasInvestigacionDAO lineasInvestigacionDAO;

	// PRUEBA
	
	@Autowired
	IdiomasDAO idiomasDAO;

	// PRUEBA
	@Override
	public void run(String... arg0) throws Exception {

		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;

		scrapData();

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		System.err.println(elapsedTime);

		System.exit(0);
	}

	@Autowired
	Extractor extractor;

	public List<Grupo> scrapData() throws InterruptedException, ExecutionException {

		//List<Grupo> gruposInicial = leerDataSet();

		// PRUEBA

		List<Grupo> gruposInicial = new ArrayList<>();
//		
//		gruposInicial.add(leerDataSetPruebas(2591L));
		
		// PRUEBA grupos nuevos 
//		gruposInicial.add(leerDataSetPruebas(21445L));
//		gruposInicial.add(leerDataSetPruebas(21596L));
//		gruposInicial.add(leerDataSetPruebas(21637L));
//		gruposInicial.add(leerDataSetPruebas(21647L));
//		gruposInicial.add(leerDataSetPruebas(21642L));
//		gruposInicial.add(leerDataSetPruebas(21618L));
//		gruposInicial.add(leerDataSetPruebas(21536L));
//		gruposInicial.add(leerDataSetPruebas(21679L));
//		gruposInicial.add(leerDataSetPruebas(13378L));
//		gruposInicial.add(leerDataSetPruebas(21612L));
//		gruposInicial.add(leerDataSetPruebas(17407L));
		//
//		gruposInicial.add(leerDataSetPruebas(2595L));
//		gruposInicial.add(leerDataSetPruebas(6997L));
//		gruposInicial.add(leerDataSetPruebas(13154L));
//		gruposInicial.add(leerDataSetPruebas(7021L));
//		gruposInicial.add(leerDataSetPruebas(12841L));   // Primeros 10 grupos
//		gruposInicial.add(leerDataSetPruebas(10091L));   // -- Tira error
//		gruposInicial.add(leerDataSetPruebas(8886L));
//		gruposInicial.add(leerDataSetPruebas(6221L));
//		gruposInicial.add(leerDataSetPruebas(16248L));
//		gruposInicial.add(leerDataSetPruebas(2593L));

//		gruposInicial.add(leerDataSetPruebas(8427L));
//		gruposInicial.add(leerDataSetPruebas(8165L));
//		gruposInicial.add(leerDataSetPruebas(2597L));
//		gruposInicial.add(leerDataSetPruebas(16907L));
//		gruposInicial.add(leerDataSetPruebas(10798L));   // Segundos 10 grupos
//		gruposInicial.add(leerDataSetPruebas(9222L));
//		gruposInicial.add(leerDataSetPruebas(1137L));
//		gruposInicial.add(leerDataSetPruebas(2512L));    // -- Tira error
//		gruposInicial.add(leerDataSetPruebas(1062L));    // -- Tira error
//		gruposInicial.add(leerDataSetPruebas(13951L));   // -- Tira error
//		
//		gruposInicial.add(leerDataSetPruebas(14026L));
//		gruposInicial.add(leerDataSetPruebas(9161L));
//		gruposInicial.add(leerDataSetPruebas(4790L));
//		gruposInicial.add(leerDataSetPruebas(8166L));	// Terceros 10 grupos
//		gruposInicial.add(leerDataSetPruebas(11324L));
//		gruposInicial.add(leerDataSetPruebas(13135L));
//		gruposInicial.add(leerDataSetPruebas(16984L));
//		gruposInicial.add(leerDataSetPruebas(16991L));
//		gruposInicial.add(leerDataSetPruebas(16858L));
//		gruposInicial.add(leerDataSetPruebas(2261L));
		
//		gruposInicial.add(leerDataSetPruebas(255L));
//		gruposInicial.add(leerDataSetPruebas(281L));
//		gruposInicial.add(leerDataSetPruebas(5922L));
//		gruposInicial.add(leerDataSetPruebas(12919L));
//		gruposInicial.add(leerDataSetPruebas(9219L));   // Cuartos 10 grupos
//		gruposInicial.add(leerDataSetPruebas(5918L));
//		gruposInicial.add(leerDataSetPruebas(2594L));
//		gruposInicial.add(leerDataSetPruebas(14027L));
//		gruposInicial.add(leerDataSetPruebas(3707L));
//		gruposInicial.add(leerDataSetPruebas(5532L));
		
//		gruposInicial.add(leerDataSetPruebas(11536L));
//		gruposInicial.add(leerDataSetPruebas(4691L));
//		gruposInicial.add(leerDataSetPruebas(7344L));
//		gruposInicial.add(leerDataSetPruebas(14019L));
//		gruposInicial.add(leerDataSetPruebas(10089L));  // Quintos 10 grupos
//		gruposInicial.add(leerDataSetPruebas(13152L));
//		gruposInicial.add(leerDataSetPruebas(21642L));
//		gruposInicial.add(leerDataSetPruebas(3041L));
//		gruposInicial.add(leerDataSetPruebas(16895L));
//		gruposInicial.add(leerDataSetPruebas(11828L));
		
//		gruposInicial.add(leerDataSetPruebas(16905L));
//		gruposInicial.add(leerDataSetPruebas(7017L));
//		gruposInicial.add(leerDataSetPruebas(2596L));
//		gruposInicial.add(leerDataSetPruebas(16908L));
//		gruposInicial.add(leerDataSetPruebas(2590L));  // Sextos 10 grupos
//		gruposInicial.add(leerDataSetPruebas(16873L));
//		gruposInicial.add(leerDataSetPruebas(6618L));
//		gruposInicial.add(leerDataSetPruebas(8164L));
//		gruposInicial.add(leerDataSetPruebas(2592L));
//		gruposInicial.add(leerDataSetPruebas(14856L));
		
//		gruposInicial.add(leerDataSetPruebas(7394L));
//		gruposInicial.add(leerDataSetPruebas(11627L));
//		gruposInicial.add(leerDataSetPruebas(16906L));
//		gruposInicial.add(leerDataSetPruebas(16910L));
//		gruposInicial.add(leerDataSetPruebas(16973L));  // Septimos 10 grupos
//		gruposInicial.add(leerDataSetPruebas(9581L));
//		gruposInicial.add(leerDataSetPruebas(6025L));
//		gruposInicial.add(leerDataSetPruebas(1424L));
//		gruposInicial.add(leerDataSetPruebas(3922L));
//		gruposInicial.add(leerDataSetPruebas(2591L));
//		
//		gruposInicial.add(leerDataSetPruebas(16976L));
//		gruposInicial.add(leerDataSetPruebas(13117L));
//		gruposInicial.add(leerDataSetPruebas(16998L));
//		gruposInicial.add(leerDataSetPruebas(14017L));
//		gruposInicial.add(leerDataSetPruebas(8163L));  // Octavos 10 grupos
//		gruposInicial.add(leerDataSetPruebas(21637L));
//		gruposInicial.add(leerDataSetPruebas(21647L));
//		gruposInicial.add(leerDataSetPruebas(21445L));
//		gruposInicial.add(leerDataSetPruebas(21618L));
//		gruposInicial.add(leerDataSetPruebas(13378L));
//		
//		gruposInicial.add(leerDataSetPruebas(21679L));
//		gruposInicial.add(leerDataSetPruebas(21536L));
//		gruposInicial.add(leerDataSetPruebas(21612L));
//		gruposInicial.add(leerDataSetPruebas(17407L)); // ultimos 5 grupos
//		gruposInicial.add(leerDataSetPruebas(21596L));
		
		// PRUEBA
		
		//idiomasDAO.deleteAll();
		
		// PRUEBA
		
		//lineasInvestigacionDAO.deleteAll();

		//investigadorDAO.deleteAll();

		
		
		List<String> urlSet = llenarUrlSet(gruposInicial);

		List<Grupo> grupos = new ArrayList<Grupo>();
		List<Future<Grupo>> resultList = new ArrayList<Future<Grupo>>();

		for (int i = 0; i < urlSet.size(); i++) {
			Future<Grupo> result = extractor.scrapData(urlSet.get(i), gruposInicial.get(i));
			resultList.add(result);
		}

		for (Future<Grupo> future : resultList) {
			try {
				grupos.add(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		// PRUEBA
		
//		for (int i = 0; i < grupos.size(); i++) {
//			if (i%5 == 0) {
//				System.out.println("-----");
//			}
//			System.out.println("ID grupos sin errores: " + grupos.get(i).getId());
//		}
		
		// PRUEBA
		
		return grupos;
	}

	public List<Grupo> leerDataSet() {
		return grupoDAO.findAll();
	}

	public Grupo leerDataSetPruebas(Long id) {
		return grupoDAO.findOne(id);
	}

	public List<String> llenarUrlSet(List<Grupo> grupos) {

		List<String> urlSet = new ArrayList<String>();
		for (int i = 0; i < grupos.size(); i++) {
			String cadena = "00000000000000" + grupos.get(i).getId();
			cadena = cadena.substring(cadena.length() - Constantes.LINK_GRUPLAC, cadena.length());
			String url = "https://scienti.colciencias.gov.co/gruplac/jsp/visualiza/visualizagr.jsp?nro=" + cadena;
			urlSet.add(url);
		}
		return urlSet;
	}
}
