export function filtersToLHSBrackets(filters){
    return Object.keys(filters).map((key) => {
        if (key && filters[key]){
            return String.prototype.concat(key, "[", filters[key][0], "]=", filters[key][1])
        }
    }).filter((elem) => {
        return elem !== undefined
    })
}

export function filtersMapToLHSBrackets(filterMap){
    return Array.from(filterMap.keys()).map((key) => {
        if (key && filterMap.get(key)) {
            return String.prototype.concat(key, "[", filterMap.get(key)[0], "]=", filterMap.get(key)[1])
        }
    }).filter((elem) => {
        return elem !== undefined
    })
}

export function parseSorterToQueryParam(sort){
    return String.prototype.concat((sort.order === 'ascend' ? '' : '-'), sort.columnKey)
}