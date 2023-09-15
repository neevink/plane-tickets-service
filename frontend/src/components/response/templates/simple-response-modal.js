import {Button, Modal, Typography} from "antd";

export function SimpleResponseModal({title, value, visible, handleOk}){
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
                {
                    value ? <Typography>Ответ: {value}</Typography> :
                            <Typography>Нет ответа</Typography>
                }
            </Modal>
        </>
    )
}