import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor, within } from "@testing-library/react";
import { MemoryRouter, Route } from "react-router-dom";

import AddSystem from "../AddSystem";
import api from "../../../services/api";

jest.mock("../../../services/api", () => ({
  addSystem: jest.fn(),
}));

const addHandler = jest.fn();

function getAliasFields() {
  const aliasesContainer = screen
    .getByText("Aliases")
    .closest(".alias-input-list");
  return within(aliasesContainer).getAllByRole("textbox");
}

describe("<AddSystem />", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("renders add system form", async () => {
    render(
      withRouting(
        <AddSystem validateNewSystem={() => undefined} onAdd={addHandler} />
      )
    );

    const element = await screen.findByText("Add a system to the register");

    expect(element).toBeInTheDocument();
  });

  it("has a page title", () => {
    render(
      withRouting(
        <AddSystem validateNewSystem={() => undefined} onAdd={addHandler} />
      )
    );

    expect(document.title).toBe("Add a system — System Register");
  });

  it("sends new system data to the API and refreshes register data on submission", async () => {
    api.addSystem.mockResolvedValue({
      id: 456,
      name: "new system",
    });
    render(
      withRouting(
        <AddSystem validateNewSystem={() => undefined} onAdd={addHandler} />
      )
    );
    const systemNameField = await screen.findByLabelText(/system name/i);
    const systemDescriptionField = await screen.findByLabelText(
      /system description/i
    );
    const aliasInputFields = getAliasFields();
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "new system");
    overtype(systemDescriptionField, "new system description");
    overtype(aliasInputFields[0], "new system alias");
    user.click(saveButton);

    await waitFor(() => {
      expect(api.addSystem).toBeCalledWith({
        name: "new system",
        description: "new system description",
        aliases: ["new system alias"],
      });
      expect(addHandler).toBeCalled();
    });
  });

  it("redirect the user to the confirmation page after successfully submission", async () => {
    api.addSystem.mockResolvedValue({
      id: 456,
      name: "new system",
    });
    render(
      withRouting(
        <AddSystem validateNewSystem={() => undefined} onAdd={addHandler} />
      )
    );
    const systemNameField = await screen.findByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "new system");
    user.click(saveButton);

    expect(
      await screen.findByRole("heading", { name: /system added/i })
    ).toBeInTheDocument();
    const systemLink = screen.getByRole("link");
    expect(systemLink).toHaveTextContent("new system");
    expect(systemLink).toHaveAttribute(
      "href",
      expect.stringMatching(/\/system\/456$/)
    );
  });

  function overtype(field, value) {
    user.clear(field);
    // noinspection JSIgnoredPromiseFromCall
    user.type(field, value);
  }

  function withRouting(component) {
    return (
      <MemoryRouter initialEntries={["/add-system"]}>
        <Route path="/add-system">{component}</Route>
      </MemoryRouter>
    );
  }
});
