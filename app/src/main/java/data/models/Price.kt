package data.models

data class Price(
    val TipoVeiculo: Int,
    val Valor: String,
    val Marca: String,
    val Modelo: String,
    val AnoModelo: Int,
    val Combustivel: String,
    val CodigoFipe: String,
    val MesReferencia: String,
    val SiglaCombustivel: String
)
