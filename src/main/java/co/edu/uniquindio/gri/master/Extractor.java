package co.edu.uniquindio.gri.master;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.gri.dao.GrupoDAO;
import co.edu.uniquindio.gri.dao.InvestigadorDAO;
import co.edu.uniquindio.gri.extractor.ExtractorApSocial;
import co.edu.uniquindio.gri.extractor.ExtractorArte;
import co.edu.uniquindio.gri.extractor.ExtractorBibliograficas;
import co.edu.uniquindio.gri.extractor.ExtractorEvaluacion;
import co.edu.uniquindio.gri.extractor.ExtractorFormacion;
import co.edu.uniquindio.gri.extractor.ExtractorGenerales;
import co.edu.uniquindio.gri.extractor.ExtractorInfoAdicional;
import co.edu.uniquindio.gri.extractor.ExtractorTecnicas;
import co.edu.uniquindio.gri.model.Grupo;
import co.edu.uniquindio.gri.model.GruposInves;
import co.edu.uniquindio.gri.model.Investigador;
import co.edu.uniquindio.gri.model.Tipo;
import co.edu.uniquindio.gri.model.TipoProduccion;
import co.edu.uniquindio.gri.utils.ArrayUtils;
import co.edu.uniquindio.gri.utils.Constantes;
import co.edu.uniquindio.gri.utils.JSoupUtil;

@Service
public class Extractor {

	@Autowired
	JSoupUtil util;

	@Autowired
	GrupoDAO grupoDAO;

	@Autowired
	InvestigadorDAO investigadorDAO;

	@Autowired
	ArrayUtils utils;

	@Async("executor1")
	public Future<Grupo> scrapData(String url, Grupo grupo) {		
		int statusConnectionCode = util.getStatusConnectionCode(url);
		
		if (statusConnectionCode == 200) {

			// Extrae todo el HTML de la url enviada por parametro
			Document document = util.getHtmlDocument(url);
			// Selecciona solo las etiquetas <table> que esten dentro de una etiqueta <body>
			Elements entradas = document.select("body>table");
			// Selecciona solo los atributos span que lleven el nombre celdaEncabezado
			Elements entradaNombre = document.select("span.celdaEncabezado");

			// Extraer Nombre del Grupo de Investigaci??n
			for (Element elem : entradaNombre) {
				
				if (elem.toString().contains("span class")) {
					grupo.setNombre(elem.text().toUpperCase());
				}
			}

			extraerDatos(entradas, grupo);

			List<GruposInves> gruposInves = grupo.getInvestigadores();
			ArrayList<Investigador> investigadores = utils.getInvestigadores();
			boolean repetido = false;

			for (int i = 0; i < gruposInves.size(); i++) {

				for (int j = 0; j < investigadores.size(); j++) {

					if (gruposInves.get(i).getInvestigador().getId() == investigadores.get(j).getId()) {
						invesRepetido(gruposInves.get(i).getInvestigador(), gruposInves.get(i).getEstado());
						repetido = true;
						break;
					}
				}

				if (!repetido) {
					investigadores.add(gruposInves.get(i).getInvestigador());
				}
				investigadorDAO.save(gruposInves.get(i).getInvestigador());
			}

			

			//////////////////////////////////////////////////////

			// for (int i = 0; i < gruposInves.size(); i++) {
			// investigadorDAO.save(gruposInves.get(i).getInvestigador(),gruposInves.get(i).getEstado());
			// }

			//////////////////////////////////////////////////////

			grupoDAO.save(grupo);
			System.err.println((grupo.getNombre()));

		} else {
			System.out.println("El Status Code no es OK es: " + statusConnectionCode);
		}

		return new AsyncResult<Grupo>(grupo);
	}

	@Autowired
	ExtractorGenerales extractor;

	@Autowired
	ExtractorBibliograficas extractorBibliograficas;

	@Autowired
	ExtractorTecnicas extractorTecnicas;

	@Autowired
	ExtractorApSocial extractorApSocial;

	@Autowired
	ExtractorFormacion extractorFormacion;

	@Autowired
	ExtractorEvaluacion extractorEvaluacion;

	@Autowired
	ExtractorInfoAdicional extractorInfoAdicional;

	@Autowired
	ExtractorArte extractorArte;

	public void extraerDatos(Elements entradas, Grupo grupo) {

		/*
		 * Extraer Datos Generales del grupo
		 */
		ArrayList<String> elemInfoGeneral = new ArrayList<>();
		for (Element elem : entradas) {
			if (elem.text().startsWith("Datos b??sicos")) {
				ArrayList<String> elemDatos = utils.ordenarArreglo(elem.toString());
				elemInfoGeneral.addAll(elemDatos);

			}
		}

		grupo = extractor.extraerDatosGeneralesG(grupo, elemInfoGeneral);

		for (Element elem : entradas) {
			
			 if (elem.text().startsWith("L??neas de investigaci??n declaradas por el grupo")) {
			 ArrayList<String> elemLineas = utils.ordenarArreglo(elem.toString());
			 extractor.extraerLineasInvestigacionG(elemLineas, grupo);
			 }
			
			 /*
			 * Extraer Lista de integrantes activos dentro del grupo de investigacion
			 */
			
			 else	if (elem.text().startsWith("Integrantes del grupo")) {
				ArrayList<String> elemIntegrantes = new ArrayList<>();
				elemIntegrantes.add(elem.toString());
				elemIntegrantes = utils.limpiarIntegrantes(elemIntegrantes);
				try {
					extractor.extraerIntegrantes(elemIntegrantes, grupo);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			}
			
			 /*
			 * Extraer Producciones bibliograficas
			 */
			
			 else if (elem.text().startsWith("Art??culos publicados")) {
			 ArrayList<String> elemArticulos = utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerArticulosG(elemArticulos, grupo);
			
			 } else if (elem.text().startsWith("Libros")) {
			 ArrayList<String> elemLibros = utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerLibrosG(elemLibros, grupo);
			
			 } else if (elem.text().startsWith("Cap??tulos de libro publicados")) {
			 ArrayList<String> elemCapLibros = utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerCapLibrosG(elemCapLibros, grupo);
			
			 } else if (elem.text().startsWith("Documentos de trabajo")) {
			 ArrayList<String> elemDocumentosTrabajo =
			 utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerDocumentosTrabajoG(elemDocumentosTrabajo,
			 grupo);
			
			 } else if (elem.text().startsWith("Otra publicaci??n divulgativa")) {
			 ArrayList<String> elemOtraProdBibliografica =
			 utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerOtraProdBibliograficaG(elemOtraProdBibliografica,
			 grupo);
			
			 } else if (elem.text().startsWith("Otros art??culos publicados")) {
			 ArrayList<String> elemOtroArticulo = utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerOtroArticuloG(elemOtroArticulo, grupo);
			
			 } else if (elem.text().startsWith("Otros Libros publicados")) {
			 ArrayList<String> elemOtroLibro = utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerOtroLibroG(elemOtroLibro, grupo);
			
			 } else if (elem.text().startsWith("Traducciones")) {
			 ArrayList<String> elemTraduccion = utils.ordenarArreglo(elem.toString());
			 extractorBibliograficas.extraerTraduccionesG(elemTraduccion, grupo);
			
			 }
			
			 /*
			 * Extraer Producciones tecnicas
			 */
			
			 else if (elem.text().startsWith("Cartas, mapas o similares")) {
			 ArrayList<String> elemMapas = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_CARTA_MAPA, Constantes.CARTA_MAPA,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemMapas, tipo, grupo);
			
			 } else if (elem.text().startsWith("Consultor??as cient??fico tecnol??gicas e Informes t??cnicos")) {
			 ArrayList<String> elemConsultorias = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_CONSULTORIA, Constantes.CONSULTORIA,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemConsultorias, tipo, grupo);
			
			 } else if (elem.text().startsWith("Dise??os industriales")) {
			 ArrayList<String> elemDisenios = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_DISE??O_INDUSTRIAL,
			 Constantes.DISE??O_INDUSTRIAL, tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemDisenios, tipo, grupo);
			
			 } else if (elem.text().startsWith("Esquemas de trazados de circuito integrado")) {
			 ArrayList<String> elemEsquemas = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_ESQUEMA_IC, Constantes.ESQUEMA_IC,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemEsquemas, tipo, grupo);
			
			 } else if (elem.text().startsWith("Innovaciones en Procesos y Procedimientos")) {
			 ArrayList<String> elemInnovacionesProc =
			 utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_INNOVACION_PROCESO,
			 Constantes.INNOVACION_PROCESO, tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemInnovacionesProc, tipo, grupo);
			
			 } else if (elem.text().startsWith("Innovaciones generadas en la Gesti??n Empresarial")) {
			 ArrayList<String> elemInnovacionesGest =
			 utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_INNOVACION_EMPRESARIAL,
			 Constantes.INNOVACION_EMPRESARIAL,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemInnovacionesGest, tipo, grupo);
			
			 } else if (elem.text().startsWith("Plantas piloto")) {
			 ArrayList<String> elemPlantasPiloto = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_PLANTA_PILOTO, Constantes.PLANTA_PILOTO,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemPlantasPiloto, tipo, grupo);
			
			 } else if (elem.text().startsWith("Otros productos tecnol??gicos")) {
			 ArrayList<String> elemOtrosProdTecno = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_OTRO_PRODUCTO_TECNOLOGICO,
			 Constantes.OTRO_PRODUCTO_TECNOLOGICO,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemOtrosProdTecno, tipo, grupo);
			
			 } else if (elem.text().startsWith("Prototipos")) {
			 ArrayList<String> elemPrototipos = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_PROTOTIPO, Constantes.PROTOTIPO,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemPrototipos, tipo, grupo);
			
			 } else if (elem.text().startsWith("Regulaciones")) {
			 ArrayList<String> elemRegulaciones = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_NORMA, Constantes.NORMA, tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemRegulaciones, tipo, grupo);
			
			 } else if (elem.text().startsWith("Reglamentos t??cnicos")) {
			 ArrayList<String> elemReglamentos = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_REGLAMENTO_TECNICO,
			 Constantes.REGLAMENTO_TECNICO, tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemReglamentos, tipo, grupo);
			
			 } else if (elem.text().startsWith("Guias de pr??ctica cl??nica")) {
			 ArrayList<String> elemGuiasClinicas = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_GUIA_CLINICA, Constantes.GUIA_CLINICA,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemGuiasClinicas, tipo, grupo);
			
			 } else if (elem.text().startsWith("Proyectos de ley")) {
			 ArrayList<String> elemProyectoLey = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_PROYECTO_LEY, Constantes.PROYECTO_LEY,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemProyectoLey, tipo, grupo);
			
			 } else if (elem.text().startsWith("Softwares")) {
			 ArrayList<String> elemSoftwares = utils.ordenarArreglo(elem.toString());
			 TipoProduccion tipoProduccion = new TipoProduccion(Constantes.ID_TECNICA,
			 Constantes.TECNICA);
			 Tipo tipo = new Tipo(Constantes.ID_SOFTWARE, Constantes.SOFTWARE,
			 tipoProduccion);
			 extractorTecnicas.extraerProdTecnicaG(elemSoftwares, tipo, grupo);
			
			 } else if (elem.text().startsWith("Empresas de base tecnol??gica")) {
			 ArrayList<String> elemEmpresasTecno = utils.ordenarArreglo(elem.toString());
			 extractorTecnicas.extraerEmpresasTecnoG(elemEmpresasTecno, grupo);
			
			 }
			
			 /*
			 * Extraer la Apropiacion social
			 */
			
			 else if (elem.text().startsWith("Ediciones")) {
			 ArrayList<String> elemEdiciones = utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerEdicionesG(elemEdiciones, grupo);
			
			 } else if (elem.text().startsWith("Informes de investigaci??n")) {
			 ArrayList<String> elemInformes = utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerInformesG(elemInformes, grupo);
			
			 } else if (elem.text().startsWith("Redes de Conocimiento Especializado")) {
			 ArrayList<String> elemRedesConocimiento =
			 utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerRedesYParticipacionG(elemRedesConocimiento, grupo);
			
			 } else if (elem.text().startsWith("Generaci??n de Contenido Impreso")) {
			 ArrayList<String> elemContImpreso = utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerContenidoImpresoMultimediaVirtualG(elemContImpreso,
			 grupo);
			
			 } else if (elem.text().startsWith("Generaci??n de Contenido Multimedia")) {
			 ArrayList<String> elemContMultimedia = utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerContenidoImpresoMultimediaVirtualG(elemContMultimedia,
			 grupo);
			
			 } else if (elem.text().startsWith("Generaci??n de Contenido Virtual")) {
			 ArrayList<String> elemContVirtual = utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerContenidoImpresoMultimediaVirtualG(elemContVirtual,
			 grupo);
			
			 } else if (elem.text().startsWith("Estrategias de Comunicaci??n del Conocimiento")) {
			 ArrayList<String> elemEstComunicacion =
			 utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerEstrategiasComunicacionG(elemEstComunicacion,
			 grupo);
			
			 } else if (elem.text().startsWith("Estrategias Pedag??gicas para el fomento a la CTI")) {
			 ArrayList<String> elemEstPedagogicasCTI =
			 utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerEstrategiasYParticipacionCtiG(elemEstPedagogicasCTI,
			 grupo);
			
			 } else if (elem.text().startsWith("Espacios de Participaci??n Ciudadana")) {
			 ArrayList<String> elemEspPartCiudadana =
			 utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerRedesYParticipacionG(elemEspPartCiudadana, grupo);
			
			 } else if (elem.text().startsWith("Participaci??n Ciudadana en Proyectos de CTI")) {
			 ArrayList<String> elemPartCiudCTI = utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerEstrategiasYParticipacionCtiG(elemPartCiudCTI,
			 grupo);
			
			 } else if (elem.text().startsWith("Eventos Cient??ficos")) {
			 ArrayList<String> elemEventos = utils.ordenarArreglo(elem.toString());
			 extractorApSocial.extraerEventosG(elemEventos, grupo);
			
			 }
			
			 /*
			 * Extraer las Actividades de Formacion
			 */
			
			 else if (elem.text().startsWith("Curso de Corta Duraci??n Dictados")) {
			 ArrayList<String> elemCursosCortaDuracion =
			 utils.ordenarArreglo(elem.toString());
			 extractorFormacion.extraerCursosCortosG(elemCursosCortaDuracion, grupo);
			
			 } else if (elem.text().startsWith("Trabajos dirigidos/turor??as")) {
			 ArrayList<String> elemTrabajosDirigidos =
			 utils.ordenarArreglo(elem.toString());
			 extractorFormacion.extraerTrabajosDirigidosG(elemTrabajosDirigidos, grupo);
			
			 }
			
			 /*
			 * Extraer las Actividades de Evaluacion
			 */
			
			 else if (elem.text().startsWith("Jurado/Comisiones evaluadoras de trabajo de grado")) {
			 ArrayList<String> elemJuradoComite = utils.ordenarArreglo(elem.toString());
			 extractorEvaluacion.extraerJuradoG(elemJuradoComite, grupo);
			
			 } else if (elem.text().startsWith("Participaci??n en comit??s de evaluaci??n"))
			 {
			 ArrayList<String> elemPartComites = utils.ordenarArreglo(elem.toString());
			 extractorEvaluacion.extraerPartipacionComitesG(elemPartComites, grupo);
			
			 }
			
			 /*
			 * Extraer Informacion adicional
			 */
			
			 else if (elem.text().startsWith("Dem??s trabajos")) {
			 ArrayList<String> elemDemasTrabajos = utils.ordenarArreglo(elem.toString());
			 extractorInfoAdicional.extraerDemasTrabajosG(elemDemasTrabajos, grupo);
			
			 } else if (elem.text().startsWith("Proyectos 1")) {
			 ArrayList<String> elemDemasTrabajos = utils.ordenarArreglo(elem.toString());
			 extractorInfoAdicional.extraerProyectosG(elemDemasTrabajos, grupo);
			
			 }
			
			 /*
			 * Extraer Producciones en Arte
			 */
			
			 else if (elem.text().startsWith("Obras o productos")) {
			 ArrayList<String> elemDemasTrabajos = utils.ordenarArreglo(elem.toString());
			 extractorArte.extraerObrasG(elemDemasTrabajos, grupo);
			
			 } else if (elem.text().startsWith("Registros de acuerdo de licencia")) {
			 ArrayList<String> elemDemasTrabajos = utils.ordenarArreglo(elem.toString());
			 extractorArte.extraerRegistrosAcuerdoG(elemDemasTrabajos, grupo);
			
			 } else if (elem.text().startsWith("Industrias Creativas y culturales")) {
			 ArrayList<String> elemDemasTrabajos = utils.ordenarArreglo(elem.toString());
			 extractorArte.extraerIndustriasG(elemDemasTrabajos, grupo);
			
			 } else if (elem.text().startsWith("Eventos art??sticos")) {
			 ArrayList<String> elemDemasTrabajos = utils.ordenarArreglo(elem.toString());
			 extractorArte.extraerEventoArtisticoG(elemDemasTrabajos, grupo);
			
			 } else if (elem.text().startsWith("Talleres Creativos")) {
			 ArrayList<String> elemDemasTrabajos = utils.ordenarArreglo(elem.toString());
			 extractorArte.extraerTallerCreativoG(elemDemasTrabajos, grupo);
			
			 }
		}
	}
	
	public void invesRepetido(Investigador investigador, String estado) {
		boolean eliminarDatos=false;
				if (!investigador.getIdiomas().isEmpty()
						|| !investigador.getLineasInvestigacion().isEmpty()
						|| !investigador.getProducciones().isEmpty()
						|| !investigador.getProduccionesBibliograficas().isEmpty()) {
					eliminarDatos = true;
				}
			
			if (eliminarDatos && estado.equals("ACTUAL")) {
				investigador.getIdiomas().clear();
				investigador.getLineasInvestigacion().clear();
				investigador.getProducciones().clear();
				investigador.getProduccionesBibliograficas().clear();
			}
	}
}
