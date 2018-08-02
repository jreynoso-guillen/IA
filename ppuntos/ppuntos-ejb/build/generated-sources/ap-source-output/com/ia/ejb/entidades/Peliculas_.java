package com.ia.ejb.entidades;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Peliculas.class)
public abstract class Peliculas_ {

	public static volatile SingularAttribute<Peliculas, Integer> idpelicula;
	public static volatile SingularAttribute<Peliculas, String> resena;
	public static volatile SingularAttribute<Peliculas, String> titulo;
	public static volatile SingularAttribute<Peliculas, byte[]> poster;
	public static volatile SingularAttribute<Peliculas, String> sinopsis;
	public static volatile SingularAttribute<Peliculas, Date> fechaestreno;

}

