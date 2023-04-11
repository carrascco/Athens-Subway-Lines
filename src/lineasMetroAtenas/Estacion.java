package lineasMetroAtenas;

public class Estacion{
	public String Estacion;
	public double gValor;
	public double hValorMin;
	public double fValor;
	public Estacion parent;
	public int linea;
	public int CoorX, CoorY;
	
	public Estacion (String nombreEstacion, int linea, double gMin, double hMin, int CoorX, int CoorY){
		Estacion = nombreEstacion;
		this.linea = linea;
		gValor = gMin;
		hValorMin = hMin;
		this.CoorX = CoorX;
		this.CoorY = CoorY;
	}

	public void setgMin(double gMin){
		gValor = gMin;
	}
	
	public void setfMin(double hMin){
		hValorMin = hMin;
	}
	
	public void sethMin(double hMin){
		hValorMin = hMin;
	}

	public void setPadre(Estacion aux) {
		parent=aux;
	}

	public void setEstacion(String nombre){
		this.Estacion=nombre;
	}
	
	public Estacion getParent() {
        return parent;
    }
	
	public String getEstacion(){
		return this.Estacion;
	}

	public double getDistancia(){
		return this.hValorMin;
	}
	
	public double getF(){
		return this.fValor;
	}
	
	public int getLinea(){
		return this.linea;
	}
	
	public int getCoorX(){
		return  this.CoorX;
	}
	
	public int getCoorY(){
		return  this.CoorY;
	}
	public double getG(){
		return this.gValor;
	}
	public void inicializarNodo(){
        fValor = hValorMin;
        gValor = 0;
    }

	public String toString(){
		return this.getEstacion() + ", " + this.getLinea() + ", " + this.getDistancia() + ", " + this.getCoorX() + this.getCoorY();
	}
}
