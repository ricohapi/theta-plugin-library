package com.theta360.pluginlibrary.values;

public enum ThetaModel {
    THETA_V("RICOH THETA V"),
    THETA_Z1("RICOH THETA Z1"),
    THETA_DEF("RICOH THETA");

    private final String mModelName;

    ThetaModel(final String modelName) {
        this.mModelName = modelName;
    }

    public static ThetaModel getValue(String _modelName) {
        for (ThetaModel thetaModel : ThetaModel.values()) {
            if (thetaModel.toString().equals(_modelName)) {
                return thetaModel;
            }
        }
        return THETA_DEF;
    }

    @Override
    public String toString() {
        return this.mModelName;
    }
}
