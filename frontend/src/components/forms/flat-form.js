import {useForm} from "antd/es/form/Form";
import {useEffect} from "react";
import {Form, Input, InputNumber, Modal, Radio, Select} from "antd";

export default function FlatForm({formVisible, onCancel, onFinish, title, initialValues}){
    const [form] = useForm()

    useEffect(() => {
        if (initialValues) {
            form.setFieldsValue(initialValues)
        }
    }, [initialValues])

    return (
        <>
            <Modal title={title}
                   open={formVisible}
                   onOk={form.submit}
                   onCancel={onCancel}
                   width={1000}
            >
                <Form form={form}
                      onFinish={onFinish}
                      labelCol={{span: 4}}
                      wrapperCol={{span: 8}}
                      layout={"horizontal"}
                >
                    <Form.Item label={"Name"}
                               name={"name"}
                               rules={[
                                   {required: true, message: "Пожалуйста введите название!"},
                                   {max: 255, message: "Максимальная длина 255!"},
                               ]}
                    >
                        <Input/>
                    </Form.Item>
                    <Form.Item label={"Area"}
                               name={"area"}
                               rules={[
                                   {required: false, message: "Пожалуйста введите площадь!"},
                                   () => ({
                                       validator(_, value){
                                           if (!value) {
                                               return Promise.resolve();
                                           }
                                           if (Number.isInteger(Number(value)) && value > 0){
                                               return Promise.resolve()
                                           }
                                           return Promise.reject(new Error("Площадь должна быть больше 0 и целым числом!"))
                                       }
                                   })
                               ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item label={"Number of rooms"}
                               name={"numberOfRooms"}
                               rules={[
                                   {required: true, message: "Пожалуйста введите количество комнат!"},
                                   () => ({
                                       validator(_, value) {
                                           if (value >= 0) {
                                               return Promise.resolve();
                                           }
                                           return Promise.reject(new Error('Количество комнат должно быть больше 0!'));
                                       },
                                   })
                               ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item label={"Floor"}
                               name={"floor"}
                               rules={[
                                   {required: true, message: "Пожалуйста введите этаж!"},
                                   () => ({
                                       validator(_, value) {
                                           if (Number.isInteger(Number(value)) && value >= 0) {
                                               return Promise.resolve();
                                           }
                                           return Promise.reject(new Error('Этаж должен быть больше 0 и целым числом!'));
                                       },
                                   })
                               ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item label={"Time to metro on foot"}
                               name={"timeToMetroOnFoot"}
                               rules={[
                                   {required: true, message: "Пожалуйста введите время до метро пешком!"},
                                   () => ({
                                       validator(_, value) {
                                           if (Number.isInteger(Number(value)) && value >= 0) {
                                               return Promise.resolve();
                                           }
                                           return Promise.reject(new Error('Время должно быть больше 0 и целым числом!'));
                                       },
                                   })
                               ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item label={"Balcony"}
                               name={"balcony"}
                               rules={[
                                   {required: true, message: "Пожалуйста выберите, есть ли у квартиры балкон!"},
                               ]}
                    >
                        <Radio.Group>
                            <Radio value={true}>Да</Radio>
                            <Radio value={false}>Нет</Radio>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item label={"View"}
                               name={"view"}
                    >
                        <Select>
                            <Select.Option value={"undefined"}>-</Select.Option>
                            <Select.Option value={"street"}>Street</Select.Option>
                            <Select.Option value={"park"}>Park</Select.Option>
                            <Select.Option value={"bad"}>Bad</Select.Option>
                            <Select.Option value={"good"}>Good</Select.Option>
                            <Select.Option value={"terrible"}>Terrible</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item label={"Price"}
                               name={"price"}
                               rules={[
                                   {required: true, message: "Пожалуйста введите цену!"},
                                   () => ({
                                       validator(_, value) {
                                           if (value >= 0) {
                                               return Promise.resolve();
                                           }
                                           return Promise.reject(new Error('Цена должна быть больше 0!'));
                                       },
                                   })
                               ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item label="Coordinates">
                        <Input.Group>
                            <Form.Item
                                label="X"
                                name={["coordinates", "x"]}
                                rules={[
                                    {required: true, message: 'Пожалуйста введите X!'},
                                    () => ({
                                        validator(_, value) {
                                            if (Number.isInteger(Number(value))) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error("X должен быть целым числом!"));
                                        },
                                    })
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                            <Form.Item
                                label="Y"
                                name={["coordinates", "y"]}
                                rules={[
                                    {required: true, message: 'Пожалуйста введите Y!'},
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                        </Input.Group>
                    </Form.Item>
                    <Form.Item label="House">
                        <Input.Group>
                            <Form.Item
                                label="Name"
                                name={["house", "name"]}
                                rules={[
                                    {required: false, message: 'Пожалуйста введите название Дома!'},
                                    {max: 255, message: "Максимальная длина 255!"},
                                ]}
                            >
                                <Input/>
                            </Form.Item>
                            <Form.Item
                                label="Year"
                                name={["house", "year"]}
                                rules={[
                                    {required: false, message: 'Пожалуйста введите год Дома!'},
                                    () => ({
                                        validator(_, value) {
                                            if (!value) {
                                                return Promise.resolve();
                                            }
                                            if (Number.isInteger(Number(value)) && value >= 0) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Год должен быть больше 0!'));
                                        },
                                    }),
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                            <Form.Item
                                label="Number of Floors"
                                name={["house", "numberOfFloors"]}
                                rules={[
                                    {required: false, message: 'Пожалуйста введите количество этажей Дома!'},
                                    () => ({
                                        validator(_, value) {
                                            if (!value) {
                                                return Promise.resolve();
                                            }
                                            if (Number.isInteger(Number(value)) && value >= 0) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Количество этажей должно быть больше 0 и целым числом!'));
                                        },
                                    }),
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                        </Input.Group>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
}