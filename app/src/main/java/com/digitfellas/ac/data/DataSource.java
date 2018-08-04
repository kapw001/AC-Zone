package com.digitfellas.ac.data;


import com.digitfellas.ac.data.local.LocalDataSource;
import com.digitfellas.ac.data.pref.Pref;
import com.digitfellas.ac.data.remote.RemoteDataSource;

/**
 * Created by yasar on 6/3/18.
 */

public interface DataSource extends RemoteDataSource, LocalDataSource, Pref {


}
