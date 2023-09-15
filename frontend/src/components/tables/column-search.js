import {Button, Col, Input, Row, Select, Space} from "antd";
import {Option} from "antd/es/mentions";
import {SearchOutlined} from "@ant-design/icons";

export const getColumnSearchProps = (dataIndex, searchFunction) => ({
    filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
        <div>
            <Row gutter={8}>
                <Col>
                    <Select value={selectedKeys[0]}
                            onChange={
                                (val) => {
                                    if (val){
                                        selectedKeys[0] = val
                                    }
                                    setSelectedKeys(selectedKeys)
                                }
                    }>
                        <Option value="eq">=</Option>
                        <Option value="neq">&ne;</Option>
                        <Option value="gt">&gt;</Option>
                        <Option value="lt">&lt;</Option>
                        <Option value="gte">&ge;</Option>
                        <Option value="lte">&le;</Option>
                    </Select>
                </Col>
                <Col>
                    <Input placeholder={`Search $(dataIndex)`}
                           value={selectedKeys[1]}
                           onChange={
                               (e) => {
                                   if (e.target.value){
                                       selectedKeys[1] = e.target.value
                                   } else {
                                       selectedKeys[1] = ""
                                   }
                                   setSelectedKeys(selectedKeys)
                               }
                           }
                    />
                </Col>
            </Row>
            <Space>
                <Button type={"primary"}
                        icon={<SearchOutlined/>}
                        size={"small"}
                        style={{
                            width: 90
                        }}
                        disabled={!selectedKeys[0] || !selectedKeys[1]}
                        onClick={
                            () => {
                                confirm()
                                searchFunction(dataIndex, selectedKeys[0], selectedKeys[1])
                            }
                        }
                >
                    Filter
                </Button>
                <Button size={"small"}
                        style={{
                            width: 90
                        }}
                        onClick={
                            () => {
                                clearFilters()
                                confirm()
                            }
                        }
                >
                    Reset
                </Button>
            </Space>
        </div>
    ),
    filterIcon: (filtered) => (
        <SearchOutlined style={{
            color: filtered ? '#1890ff' : undefined
        }}/>
    )
})