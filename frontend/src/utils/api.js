// const CATALOG_SERVICE = "http://localhost:41147/catalog"
// const AGENCY_SERVICE = "http://localhost:41148/agency"

const CATALOG_SERVICE = "https://localhost:4500/api/v1/catalog"
const AGENCY_SERVICE = "https://localhost:4600/api/v1/agency"

export const FLATS_API = `${CATALOG_SERVICE}/flats`
export const DELETE_ONE_FLAT_BY_VIEW = `${FLATS_API}/delete-one-by-view`
export const AVERAGE_TIME_TO_METRO = `${FLATS_API}/average-time-metro`
export const UNIQUE_VIEW = `${FLATS_API}/unique-view`

export const FIND_WITH_BALCONY = `${AGENCY_SERVICE}/find-with-balcony`
export const GET_MOST_EXPENSIVE = `${AGENCY_SERVICE}/get-most-expensive`
