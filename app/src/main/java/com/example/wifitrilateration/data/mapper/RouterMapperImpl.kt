package com.example.wifitrilateration.data.mapper

import com.example.wifitrilateration.domain.entity.FeatureLatLng
import com.example.wifitrilateration.domain.entity.Router
import com.glebalekseevjk.yasmrhomework.data.local.model.RouterDbModel
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import javax.inject.Inject


class RouterMapperImpl @Inject constructor() : Mapper<Router, RouterDbModel> {
    override fun mapItemToDbModel(item: Router): RouterDbModel {
        with(item) {
            return RouterDbModel(
                bssid,
                level,
                coordinates.lat,
                coordinates.lng
            )
        }
    }

    override fun mapDbModelToItem(dbModel: RouterDbModel): Router {
        with(dbModel) {
            return Router(
                bssid,
                level,
                FeatureLatLng(lat,lng)
            )
        }
    }
}