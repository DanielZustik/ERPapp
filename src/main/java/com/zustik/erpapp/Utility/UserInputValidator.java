package com.zustik.erpapp.Utility;

import com.zustik.erpapp.ControllersPresentation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInputValidator {

    public UserInputValidator() {
    }

    public AtomicBoolean inputValidationOnAddDialog(FXMLLoader fxmlLoader, Dialog<ButtonType> dialog) {

        Object controller = fxmlLoader.getController(); //type at declaration
        AtomicBoolean isInvalidInput = new AtomicBoolean(true);

//        if (controller instanceof EmployeesDialogs) {
//            EmployeesDialogs employeesDialogs = (EmployeesDialogs) controller;
//        } else {
//            InvoicesDialogs invoicesDialogs = (InvoicesDialogs) controller;
//        }

        if (controller instanceof EmployeesDialogAddController) {  //runtime type check
            EmployeesDialogAddController addController = (EmployeesDialogAddController) controller;
            isInvalidInput = inputValidationProcessingEmployees(dialog, addController);
        } else if (controller instanceof EmployeesDialogEditController) {
            EmployeesDialogEditController editController = (EmployeesDialogEditController) controller;
            isInvalidInput = inputValidationProcessingEmployees(dialog, editController);

        } else if (controller instanceof InvoicesDialogAddController) {
            InvoicesDialogAddController addController = (InvoicesDialogAddController) controller;
            isInvalidInput = inputValidationProcessingInvoices(fxmlLoader, dialog, addController);
        } else if (controller instanceof InvoicesDialogEditController) {
            InvoicesDialogEditController editController = (InvoicesDialogEditController) controller;
            isInvalidInput = inputValidationProcessingInvoices(fxmlLoader, dialog, editController);
        }
        return isInvalidInput;
    }

    public AtomicBoolean inputValidationProcessingInvoices(FXMLLoader fxmlLoader, Dialog<ButtonType> dialog, InvoicesDialogs controller) {
        AtomicBoolean invalidInput = new AtomicBoolean(false);
        System.out.println(controller.getDueDatePicker().getStyleClass()); //pro zjisteni jake css classy jsou defaultne aplikovane abcyh se k nim mohl vratit
//        String invoiceNumber = controller.getInvoiceNumberField().getText().trim();
//        String dueAmount = controller.getDueAmountField().getText().trim();
//        String totalAmount = controller.getTotalAmountField().getText().trim();
//        String subject = controller.getSubjectField().getText().trim();
//        String recipient = controller.getRecipientField().getText().trim();
        AtomicBoolean parsingWasNotSuccessful = new AtomicBoolean(false);

        dialog.getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION,
                    actionEvent -> {
            //its ok to try parse it before checking if its null because there cant be null more below
                        try {
                            Integer.parseInt(controller.getInvoiceNumberField().getText().trim());

                        } catch (NumberFormatException e) {
                            parsingWasNotSuccessful.set(true);
                        }
                        String allowedPattern = "^2024\\d{4}$";
                        Pattern r = Pattern.compile(allowedPattern);
                        Matcher m = r.matcher(controller.getInvoiceNumberField().getText().trim());
                        //unnecessary check for null, textFields return ""
                        if (controller.getInvoiceNumberField().getText().trim().isEmpty() || parsingWasNotSuccessful.get()
                        || !m.matches()) {
                            controller.getInvoiceNumberField().getStyleClass().add("redBackground");
                            controller.getInvoiceNumberField().textProperty()
                                    .addListener(new ChangeListener<String>() {
                                                                                  @Override
                                                                                  public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                                                                                      controller.getInvoiceNumberField().getStyleClass().clear();
                                                                                      controller.getInvoiceNumberField().getStyleClass().addAll("text-field", "text-input");
                                                                                  }
                                                                              }


//                                    (observableValue, old, newValue) -> {
//                                        controller.getInvoiceNumberField().getStyleClass().clear();
//                                        controller.getInvoiceNumberField().getStyleClass().addAll("text-field", "text-input");
//
//                                    }
                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                            parsingWasNotSuccessful.set(false); //reseting
                        }


                        if (controller.getRecipientField().getText().trim().isEmpty()) {
                            controller.getRecipientField().getStyleClass().add("redBackground");
                            controller.getRecipientField().textProperty().addListener(
                                    (observableValue, old, newValue) -> {
                                        controller.getRecipientField().getStyleClass().clear();
                                        controller.getRecipientField().getStyleClass().addAll("text-field", "text-input");
                                    }

                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                        }


                        if (controller.getSubjectField().getText().trim().isEmpty()) {
                            controller.getSubjectField().getStyleClass().add("redBackground");
                            controller.getSubjectField().textProperty().addListener(
                                    (observableValue, old, newValue) -> {
                                        controller.getSubjectField().getStyleClass().clear();
                                        controller.getSubjectField().getStyleClass().addAll("text-field", "text-input");
                                    }

                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                        }


                        try {
                            Double.parseDouble(controller.getTotalAmountField().getText().trim());
                        } catch (NumberFormatException e) {
                            parsingWasNotSuccessful.set(true);
                        }
                         catch (NullPointerException e) {
                            //just catching becasue null is dealt already with controller.getTotalAmountField().getText().trim().isEmpty()
                        }
                        if (controller.getTotalAmountField().getText().trim().isEmpty() || parsingWasNotSuccessful.get()) {
                            controller.getTotalAmountField().getStyleClass().add("redBackground");
                            controller.getTotalAmountField().textProperty().addListener(
                                    (observableValue, old, newValue) -> {
                                        controller.getTotalAmountField().getStyleClass().clear();
                                        controller.getTotalAmountField().getStyleClass().addAll("text-field", "text-input");
                                    }

                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                            parsingWasNotSuccessful.set(false); //reseting
                        }


                        try {
                            Double.parseDouble(controller.getDueAmountField().getText().trim());
                        } catch (NumberFormatException e) {
                            parsingWasNotSuccessful.set(true);
                        }
                        catch (NullPointerException e) {
                            //ok dealt with below
                        }
                        if (controller.getDueAmountField().getText().trim().isEmpty() || parsingWasNotSuccessful.get()) {
                            controller.getDueAmountField().getStyleClass().add("redBackground");
                            controller.getDueAmountField().textProperty().addListener(
                                    (observableValue, old, newValue) -> {
                                        controller.getDueAmountField().getStyleClass().clear();
                                        controller.getDueAmountField().getStyleClass().addAll("text-field", "text-input");
                                    }

                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                            parsingWasNotSuccessful.set(false); //reseting

                        }


                        if (controller.getIsssueDateField().getValue() != null) {
                            try {
                                String input = controller.getIsssueDateField().getEditor().getText();
                                System.out.println(input);
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
                                LocalDate date = LocalDate.parse(input,formatter);

                            }
                            catch (DateTimeParseException e) {
                                controller.getIsssueDateField().getStyleClass().add("redBackground");
                                controller.getIsssueDateField().valueProperty().addListener(
                                        (observableValue, old, newValue) -> {
                                            controller.getIsssueDateField().getStyleClass().clear();
                                            controller.getIsssueDateField().getStyleClass().addAll("combo-box-base, date-picker");
                                        }

                                );
                                actionEvent.consume();
                                invalidInput.set(true);
                            }
                        }

                        if (controller.getIsssueDateField().getValue() == null) {
                            controller.getIsssueDateField().getStyleClass().add("redBackground");
                            controller.getIsssueDateField().valueProperty().addListener(
                                    (observableValue, old, newValue) -> {
                                        controller.getIsssueDateField().getStyleClass().clear();
                                        controller.getIsssueDateField().getStyleClass().addAll("combo-box-base, date-picker");
                                    }

                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                        }


                        if (controller.getDueDatePicker().getValue() != null) {
                            try {
                                String input = controller.getDueDatePicker().getEditor().getText();
                                System.out.println(input);
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
                                LocalDate date = LocalDate.parse(input,formatter);

                            }
                            catch (DateTimeParseException e) {
                                controller.getDueDatePicker().getStyleClass().add("redBackground");
                                controller.getDueDatePicker().valueProperty().addListener(
                                        (observableValue, old, newValue) -> {
                                            controller.getDueDatePicker().getStyleClass().clear();
                                            controller.getDueDatePicker().getStyleClass().addAll("combo-box-base, date-picker");
                                        }

                                );
                                actionEvent.consume();
                                invalidInput.set(true);
                            }
                        }

                        if (controller.getDueDatePicker().getValue() == null) {
                            controller.getDueDatePicker().getStyleClass().add("redBackground");
                            controller.getDueDatePicker().valueProperty().addListener(
                                    (observableValue, old, newValue) -> {
                                        controller.getDueDatePicker().getStyleClass().clear();
                                        controller.getDueDatePicker().getStyleClass().addAll("combo-box-base, date-picker");
                                    }

                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                        }


                        try {
                            Integer.parseInt(controller.getProjectNumberField().getText().trim());
                        } catch (NumberFormatException e) {
                            parsingWasNotSuccessful.set(true);
                        }
                        catch (NullPointerException e) {
                        }
                        if (parsingWasNotSuccessful.get() && !controller.getProjectNumberField().getText().trim().isEmpty()) { //throws numberform excp when field is empty
                            controller.getProjectNumberField().getStyleClass().add("redBackground");
                            controller.getProjectNumberField().textProperty().addListener(
                                    (observableValue, old, newValue) -> {
                                        controller.getProjectNumberField().getStyleClass().clear();
                                        controller.getProjectNumberField().getStyleClass().addAll("text-field", "text-input");
                                    }

                            );
                            actionEvent.consume();
                            invalidInput.set(true);
                            parsingWasNotSuccessful.set(false); //reseting

                        }
                        parsingWasNotSuccessful.set(false); //reseting because it would trgigger first one if with invoice field again to positive... not very effective to use just one variable for all fields...
                    });
//            inputValidationOnAddDialogAllowedCharacters(fxmlLoader, dialog);
            return invalidInput;
    }

    public AtomicBoolean inputValidationProcessingEmployees(Dialog<ButtonType> dialog,
                                                            EmployeesDialogs controller) {



        AtomicBoolean invalidInput = new AtomicBoolean(false);
        System.out.println(controller.getActiveStatusYes().getStyleClass()); //pro zjisteni jake css classy jsou defaultne aplikovane abcyh se k nim mohl vratit
        AtomicBoolean parsingWasNotSuccessful = new AtomicBoolean(false);

        dialog.getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION,
                actionEvent -> {
                    if (controller.getFirstName().getText().trim().isEmpty()) {
                        controller.getFirstName().getStyleClass().add("redBackground");
                        controller.getFirstName().textProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getFirstName().getStyleClass().clear();
                                    controller.getFirstName().getStyleClass().addAll("text-field", "text-input");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }


                    if (controller.getLastName().getText().trim().isEmpty()) {
                        controller.getLastName().getStyleClass().add("redBackground");
                        controller.getLastName().textProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getLastName().getStyleClass().clear();
                                    controller.getLastName().getStyleClass().addAll("text-field", "text-input");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }

                    if (controller.getContractType().getSelectionModel().getSelectedItem() == null) {
                        controller.getContractType().getStyleClass().add("redBackground");
                        controller.getContractType().valueProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getContractType().getStyleClass().clear();
                                    controller.getContractType().getStyleClass().addAll("choice-box");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }

                    if (controller.getDepartment().getText().isEmpty()) {
                        controller.getDepartment().getStyleClass().add("redBackground");
                        controller.getDepartment().textProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getDepartment().getStyleClass().clear();
                                    controller.getDepartment().getStyleClass().addAll("text-field", "text-input");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }

                    if (controller.getActiveStatus().getSelectedToggle() == null) {
                        controller.getActiveStatusYes().getStyleClass().add("redBorder");
                        controller.getActiveStatusNo().getStyleClass().add("redBorder");
                        controller.getActiveStatusYes().selectedProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getActiveStatusYes().getStyleClass().clear();
                                    controller.getActiveStatusYes().getStyleClass().addAll("radio-button");
                                    controller.getActiveStatusNo().getStyleClass().clear();
                                    controller.getActiveStatusNo().getStyleClass().addAll("radio-button");
                                }

                        );
                        controller.getActiveStatusNo().selectedProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getActiveStatusNo().getStyleClass().clear();
                                    controller.getActiveStatusNo().getStyleClass().addAll("radio-button");
                                    controller.getActiveStatusYes().getStyleClass().clear();
                                    controller.getActiveStatusYes().getStyleClass().addAll("radio-button");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }

                    if (controller.getActiveStatusNo().isSelected() && controller.getTerminationDate().getValue() == null) {
                        controller.getTerminationDate().getStyleClass().add("redBackground");
                        controller.getTerminationDate().valueProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getTerminationDate().getStyleClass().clear();
                                    controller.getTerminationDate().getStyleClass().addAll("combo-box-base, date-picker");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }

                    try {
                        Double.parseDouble(controller.getGrossWage().getText().trim());
                    } catch (NumberFormatException e) {
                        parsingWasNotSuccessful.set(true);
                    }
                    catch (NullPointerException e) {
                        //just catching becasue null is dealt already with controller.getTotalAmountField().getText().trim().isEmpty()
                    }
                    if (controller.getGrossWage().getText().trim().isEmpty() || parsingWasNotSuccessful.get()) {
                        controller.getGrossWage().getStyleClass().add("redBackground");
                        controller.getGrossWage().textProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getGrossWage().getStyleClass().clear();
                                    controller.getGrossWage().getStyleClass().addAll("text-field", "text-input");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                        parsingWasNotSuccessful.set(false); //reseting
                    }

                    if (controller.getHireDate().getValue() == null) {
                        controller.getHireDate().getStyleClass().add("redBackground");
                        controller.getHireDate().valueProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getHireDate().getStyleClass().clear();
                                    controller.getHireDate().getStyleClass().addAll("combo-box-base, date-picker");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }

                    /////////////////////////fields that can be empty or "" but with right data type//////////////
                    if (!controller.getNumberOfChildren().getText().isEmpty() && !controller.getNumberOfChildren().getText().matches("\\d+")) {
                        controller.getNumberOfChildren().getStyleClass().add("redBackground");
                        controller.getNumberOfChildren().textProperty().addListener(
                                (observableValue, old, newValue) -> {
                                    controller.getNumberOfChildren().getStyleClass().clear();
                                    controller.getNumberOfChildren().getStyleClass().addAll("text-field", "text-input");
                                }

                        );
                        actionEvent.consume();
                        invalidInput.set(true);
                    }
                    //////////////////////////////////////////////////////////////////////////////////////////////

                    parsingWasNotSuccessful.set(false); //reseting because it would trgigger first one if with invoice field again to positive... not very effective to use just one variable for all fields...
                });
//            inputValidationOnAddDialogAllowedCharacters(fxmlLoader, dialog);
        return invalidInput;
    }


    }



