import {useSnackbar} from "notistack";
import {useEffect, useState} from "react";
import axios from "axios";
import {FLATS_API} from "../../utils/api"
import {Button, Layout, Space, Table, Tag} from "antd";
import {ReloadOutlined} from "@ant-design/icons";
import {filtersMapToLHSBrackets, filtersToLHSBrackets, parseSorterToQueryParam} from "../../utils/tables/sort-and-filter";
import {getColumnSearchProps} from "./column-search";

const {Sider} = Layout;

export default function FlatsTable({pageSize}){
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const [sortQueryParams, setSortQueryParams] = useState([]);
    const [filterModel, setFilterModel] = useState(new Map());
    const [currentPage, setCurrentPage] = useState(1);
    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [loading, setLoading] = useState(true);
    const [collapsed, setCollapsed] = useState(false)

    useEffect(() => {
        getData(1, pageSize);
    }, [])

    const getData = (page, sort, filter) => {
        const queryParams = new URLSearchParams();

        queryParams.append("page", page);
        queryParams.append("pageSize", pageSize);

        if (sort?.length){
            sort.forEach((elem) => {
                queryParams.append("sort", elem)
            })
        }

        if (filter) {
            filter.forEach((elem) => {
                queryParams.append("filter", elem)
            })
        }

        setLoading(true)

        axios.get(FLATS_API, {params: queryParams})
            .then((response) => {
                setData(response.data["flatGetResponseDtos"].map((elem) =>{
                    return {
                        ...elem,
                        key: elem.id
                    }
                }))
                setTotalCount(response.data["totalCount"])
                setLoading(false)
            })
            .catch((err) => {
                let error = err.response.data
                enqueueSnackbar(error.message, {
                    autoHideDuration: 5000,
                    variant: "error"
                })
                if (error){
                    setLoading(false)
                }
            })
    }

    const handleTableChange = (pagination, filters, sorter) => {
        let sortQueryParams = []
        let filterParams = []

        const sortersArray = Array.from(sorter)

        if (sorter.hasOwnProperty("column")) {
            sortQueryParams[0] = parseSorterToQueryParam(sorter)
        } else if (sortersArray.length > 0){
            sortQueryParams = sortersArray.map((elem) => {
                return parseSorterToQueryParam(elem)
            })
        }

        if (filters) {
            filterParams = filtersToLHSBrackets(filters)
        }

        let newFilterModel = new Map()

        Object.keys(filters).forEach((key) => {
            if (key && filters[key]) {
                newFilterModel.set(key, [String([filters[key][0]]), filters[key][1]])
            }
        })

        setCurrentPage(pagination.current)
        setSortQueryParams(sortQueryParams)
        setFilterModel(newFilterModel)

        getData(pagination.current, sortQueryParams, filterParams)
    }

    const handleFilterChange = (dataIndex, filterType, filterValue) => {
        if (filterType && filterValue) {
            const filtrationChanged = filterModel?.get(dataIndex) &&
                (
                    filterModel?.get(dataIndex)[0] !== filterType ||
                    filterModel?.get(dataIndex)[1] !== filterValue
                )

            if (filtrationChanged) {
                filterModel.set(dataIndex, [filterType, filterValue])
                getData(currentPage, sortQueryParams, filtersMapToLHSBrackets(filterModel))
                setFilterModel(filterModel)
            }
        }
    }

    const handleRefresh = () => {
        getData(currentPage, sortQueryParams, filtersMapToLHSBrackets(filterModel))
    }

    return (
        <>
            <Table columns={[
                {
                    title: "ID",
                    dataIndex: "id",
                    key: "id",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    fixed: "left",
                    ...getColumnSearchProps("id", handleFilterChange)
                },
                {
                    title: "Name",
                    dataIndex: "name",
                    key: "name",
                    sorter: {multiple: 4},
                    sortDirections: ["ascend", "descend"],
                    // fixed: "left",
                    ...getColumnSearchProps("name", handleFilterChange)
                },
                {
                    title: "Creation date",
                    dataIndex: "creationDate",
                    key: "creationDate",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    ...getColumnSearchProps("creationDate", handleFilterChange),
                    children: [
                        {
                            title: "Time",
                            dataIndex: ["creationDate"],
                            key: "creationDate.time",
                            render: (text, row) => {
                                let date = text.split("T")[1].split(".")
                                return `${date[0]}`
                            }
                        },
                        {
                            title: "Date",
                            dataIndex: ["creationDate"],
                            key: "creationDate.date",
                            render: (text, row) => {
                                let date = text.split("T")
                                return `${date[0]}`
                            }
                        },
                        // {
                        //     title: "Day",
                        //     dataIndex: ["creationDate", 2],
                        //     key: "creationDate.day",
                        // },
                        // {
                        //     title: "Month",
                        //     dataIndex: ["creationDate", 1],
                        //     key: "creationDate.month",
                        // },
                        // {
                        //     title: "Year",
                        //     dataIndex: ["creationDate", 0],
                        //     key: "creationDate.year",
                        // },
                    ]
                },
                {
                    title: "Area",
                    dataIndex: "area",
                    key: "area",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    ...getColumnSearchProps("area", handleFilterChange)
                },
                {
                    title: "Number of rooms",
                    dataIndex: "numberOfRooms",
                    key: "numberOfRooms",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    ...getColumnSearchProps("numberOfRooms", handleFilterChange)
                },
                {
                    title: "Floor",
                    dataIndex: "floor",
                    key: "floor",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    ...getColumnSearchProps("floor", handleFilterChange)
                },
                {
                    title: "Time to metro on foot",
                    dataIndex: "timeToMetroOnFoot",
                    key: "timeToMetroOnFoot",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    ...getColumnSearchProps("timeToMetroOnFoot", handleFilterChange)
                },
                {
                    title: "Balcony",
                    dataIndex: "balcony",
                    key: "balcony",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    render: (text, record) => (
                        <Tag color={text ? 'green' : 'volcano'}>
                            {text.toString()}
                        </Tag>
                    ),
                    ...getColumnSearchProps("balcony", handleFilterChange)
                },
                {
                    title: "View",
                    dataIndex: "view",
                    key: "view",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    ...getColumnSearchProps("view", handleFilterChange)
                },
                {
                    title: "Price",
                    dataIndex: "price",
                    key: "price",
                    sorter: {multiple: 3},
                    sortDirections: ["ascend", "descend"],
                    ...getColumnSearchProps("price", handleFilterChange)
                },
                {
                    title: "Coordinates",
                    children: [
                        {
                            title: "X",
                            dataIndex: ["coordinates", "x"],
                            key: "coordinates.x",
                            sorter: {multiple: 3},
                            sortDirections: ["ascend", "descend"],
                            ...getColumnSearchProps("coordinates.x", handleFilterChange)
                        },
                        {
                            title: "Y",
                            dataIndex: ["coordinates", "y"],
                            key: "coordinates.y",
                            sorter: {multiple: 3},
                            sortDirections: ["ascend", "descend"],
                            ...getColumnSearchProps("coordinates.y", handleFilterChange)
                        },
                    ]
                },
                {
                    title: "House",
                    children: [
                        {
                            title: "Name",
                            dataIndex: ["house", "name"],
                            key: "house.name",
                            sorter: {multiple: 3},
                            sortDirections: ["ascend", "descend"],
                            ...getColumnSearchProps("house.name", handleFilterChange)
                        },
                        {
                            title: "Number of floors",
                            dataIndex: ["house", "numberOfFloors"],
                            key: "house.numberOfFloors",
                            sorter: {multiple: 3},
                            sortDirections: ["ascend", "descend"],
                            ...getColumnSearchProps("house.numberOfFloors", handleFilterChange)
                        },
                        {
                            title: "Year",
                            dataIndex: ["house", "year"],
                            key: "house.year",
                            sorter: {multiple: 3},
                            sortDirections: ["ascend", "descend"],
                            ...getColumnSearchProps("house.year", handleFilterChange)
                        },
                    ]
                }
            ]}
                   rowKey={"key"}
                   dataSource={data}
                   onChange={handleTableChange}
                   loading={loading}
                   bordered={true}
                   scroll={{x: 'max-content'}}
                   pagination={{
                       total: totalCount,
                       pageSize: pageSize,
                       showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} items`
                   }}
            />
            <Space style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
            }}>
                <Button icon={<ReloadOutlined/>} onClick={handleRefresh} style={{}}>
                    Refresh
                </Button>
            </Space>
        </>
    )
}