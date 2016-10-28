package aima.core.environment.hidato;

import aima.core.agent.Action;
import aima.core.search.framework.StepCostFunction;

/**
 * Clase que implementa la funcion que decide el coste de una accion.
 */
public class CosteAccion implements StepCostFunction{

	@Override
	public double c(Object s, Action a, Object sDelta) {
		Estado estado = (Estado)s; //Estado sobre el que se aplica la accion
		Estado estadoSucesor = (Estado)s; //Estado al que se llega tras aplicar la accion
		Accion accion = (Accion)a; //Accion a ejecutar cuyo coste se debe devolver
		
		double cost = 1; //Calcular el coste de la accion sobre el estado
		
		/*Si el coste se asocia a la instancia de Accion, aquí se puede simplemente
		 devolver el atributo de dicha instancia */

		return cost;
		
	}
	

}
