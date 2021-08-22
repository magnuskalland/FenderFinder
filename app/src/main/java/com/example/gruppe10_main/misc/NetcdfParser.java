package com.example.gruppe10_main.misc;

import android.util.Log;

import com.example.gruppe10_main.dataclasses.LostObjectEnum;
import com.example.gruppe10_main.dataclasses.NetcdfEndpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.nc2.Variable;

public class NetcdfParser {

    private final String TAG = "MAIN_ACTIVITY";

    /**
     * Method for parsing CDM file
     * @param pathname absolute path for file to be parsed
     * @return arraylist with NetcdfEndpoint objects for every leeway, containing status, lon and lat
     */
    public ArrayList<NetcdfEndpoint> parse(String pathname) {
        NetcdfFile ncfile;
        ArrayList<NetcdfEndpoint> endpoints = new ArrayList<>();

        try {
            ncfile = NetcdfFiles.open(pathname);

            ArrayList<Integer> index = findEndIndex(
                    Objects.requireNonNull(ncfile.findVariable("status")));

            ArrayList<Double> status = findEndpoint(
                    Objects.requireNonNull(ncfile.findVariable("status")), index);
            ArrayList<Double> lon = findEndpoint(
                    Objects.requireNonNull(ncfile.findVariable("lon")), index);
            ArrayList<Double> lat = findEndpoint(
                    Objects.requireNonNull(ncfile.findVariable("lat")), index);


            for (int i = 0; i < status.size(); i++) {
                NetcdfEndpoint endpointObject =
                        new NetcdfEndpoint(isActive(status.get(i)), lat.get(i), lon.get(i));
                endpoints.add(endpointObject);
            }

        } catch (IOException ioe) {
            Log.d(TAG, ioe.toString());
        }
        return endpoints;
    }

    /**
     *
     * @param variable 'status' from netcdf file
     * @return ArrayList with index for the last point in every leeway
     * @throws IOException from Array.read()
     */
    private ArrayList<Integer> findEndIndex(Variable variable) throws IOException {
        Array data = variable.read();

        ArrayList<Integer> endpointIndex = new ArrayList<>();

        int [] dataShape = data.getShape();
        Index index = data.getIndex();

        for(int i = 0; i < dataShape[0]; i++){
            ArrayList<Integer> path = new ArrayList<>();
            for (int j = 0; j < dataShape[1]; j++){
                if (data.getDouble(index.set(i,j)) == 0 || data.getDouble(index.set(i,j)) == 1){
                    path.add(j);
                }
            }
            endpointIndex.add(path.get(path.size()-1));
        }
        return endpointIndex;
    }


    /**
     *
     * @param variable from netcdf file
     * @param endIndex list with indexnumber for the last point in every leeway
     * @return arraylist with last value in variable for every leeway
     * @throws IOException from Array.read()
     */
    private ArrayList<Double> findEndpoint(Variable variable, ArrayList<Integer> endIndex) throws IOException {
        Array data = variable.read();
        ArrayList<Double> endpoint = new ArrayList<>();

        Index index = data.getIndex();

        for(int i = 0; i < endIndex.size(); i++){
            endpoint.add(data.getDouble(index.set(i, endIndex.get(i))));
        }
        return endpoint;
    }

    private LostObjectEnum isActive(Double status) {
        if (status == 0) return LostObjectEnum.ACTIVE;
        if (status == 1) return LostObjectEnum.STRANDED;
        else return LostObjectEnum.ORIGIN;
    }
}
