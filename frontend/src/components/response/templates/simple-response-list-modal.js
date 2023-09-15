import {Button, List, Modal} from "antd";

export function SimpleResponseListModal({title, list, visible, handleOk}){
    return (
        <>
            <Modal open={visible}
                   title={title}
                   onCancel={handleOk}
                   footer={[
                       <Button type={"primary"} key={"submit"} onClick={handleOk}>
                           Ок
                       </Button>
                   ]}

            >
                <List size={"small"}
                      hader={<div>Список</div>}
                      bordered
                      dataSource={list}
                      renderItem={item => <List.Item>{item}</List.Item>}
                />
            </Modal>
        </>
    )
}