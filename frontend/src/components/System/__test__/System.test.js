import React from "react";
import user from "@testing-library/user-event";
import { useKeycloak } from "@react-keycloak/web";
import { render, screen, waitFor } from "@testing-library/react";
import { createMemoryHistory } from "history";
import { forEach, omit } from "lodash-es";
import { ErrorBoundary } from "react-error-boundary";
import { Route, Router } from "react-router-dom";

import PageNotFoundError from "../../Errors/PageNotFoundError";
import System from "../System";
import SystemNotFoundException from "../../../services/systemNotFoundException";
import api from "../../../services/api";
import { DateTime } from "luxon";

jest.mock("../../../services/api", () => ({
  getSystem: jest.fn(),
  updateSystemName: jest.fn(),
  updateSystemDescription: jest.fn(),
  updateSystemAliases: jest.fn(),
  updatePortfolio: jest.fn(),
  updateCriticality: jest.fn(),
  updateInvestmentState: jest.fn(),
  updateDevelopedBy: jest.fn(),
  updateSupportedBy: jest.fn(),
  updateBusinessOwner: jest.fn(),
  updateProductOwner: jest.fn(),
  updateTechnicalOwner: jest.fn(),
  updateServiceOwner: jest.fn(),
  updateInformationAssetOwner: jest.fn(),
  updateRisk: jest.fn(),
  updateSunset: jest.fn(),
}));
jest.mock("@react-keycloak/web", () => ({
  useKeycloak: jest.fn(),
}));

const test_system = {
  name: "Test System",
  description: "The description",
  portfolio: "original portfolio",
  criticality: "low",
  investment_state: "invest",
  last_updated: {},
  risks: [
    { name: "lens1", rationale: "rationale1" },
    { name: "lens2", rationale: "rationale2" },
  ],
  aliases: [],
  sunset: {
    date: null,
    additional_information: null,
  },
};

const changeHandler = jest.fn();

describe("<System />", () => {
  beforeEach(() => {
    jest.resetAllMocks();
    api.getSystem.mockResolvedValue(test_system);
  });

  it("renders system view", async () => {
    renderWithRouting("1");

    const element = await screen.findByText("Test System");

    expect(element).toBeInTheDocument();
  });

  describe("Exceptions", () => {
    beforeEach(() => {
      jest.spyOn(console, "error");
      console.error.mockImplementation(() => null);
    });

    afterEach(() => {
      console.error.mockRestore();
    });

    it("renders page not found error view when api throws SystemNotFoundException", async () => {
      api.getSystem.mockResolvedValue(() => {
        throw new SystemNotFoundException();
      });
      render(
        <Router history={createMemoryHistory()}>
          <ErrorBoundary fallback={<PageNotFoundError />}>
            <System
              portfolios={[]}
              onBeforeNameChange={() => {}}
              onChange={changeHandler}
            />
          </ErrorBoundary>
        </Router>
      );

      const element = await screen.findByText("Page not found");

      expect(element).toBeInTheDocument();
    });
  });

  describe("when authorized", () => {
    beforeEach(() => {
      useKeycloak.mockReturnValue({
        keycloak: {
          authenticated: true,
        },
        initialized: true,
      });
    });

    it("renders page not found error view when path to update path is incorrect", async () => {
      renderWithRouting("123/update-blargh");

      const element = await screen.findByText("Page not found");

      expect(element).toBeInTheDocument();
    });

    it("shows an edit view for info", async () => {
      renderWithRouting("1/update-info");

      const element = await screen.findByText("Test System");

      expect(element).toBeInTheDocument();
    });

    it("shows an edit view for about", async () => {
      renderWithRouting("1/update-about");

      const element = await screen.findByText("Test System");

      expect(element).toBeInTheDocument();
    });

    it("shows an edit view for key dates", async () => {
      renderWithRouting("1/update-key-dates");

      const element = await screen.findByText("Test System");

      expect(element).toBeInTheDocument();
    });

    it("shows an edit view for contacts", async () => {
      renderWithRouting("1/update-contacts");

      const element = await screen.findByText("Test System");

      expect(element).toBeInTheDocument();
    });

    it.each(test_system.risks)(
      "shows an edit view for risks: %p",
      async ({ name }) => {
        renderWithRouting(`1/update-risk?lens=${name}`);

        const element = await screen.findByText("Test System");

        expect(element).toBeInTheDocument();
      }
    );

    it("hides the status message after it’s been viewed", async () => {
      api.updateSystemName.mockResolvedValue({
        ...test_system,
        name: "a new name",
      });
      const { history } = renderWithRouting("123/update-info");
      const systemNameField = await screen.findByLabelText(/system name/i);
      const saveButton = screen.getByRole("button", { name: /save/i });
      overtype(systemNameField, "a new name");
      user.click(saveButton);
      expect(
        await screen.findByText(/update has been saved/i)
      ).toBeInTheDocument();

      history.go(-1);
      history.go(1);

      await screen.findByRole("heading", { name: "a new name" });
      expect(
        screen.queryByText(/update has been saved/i)
      ).not.toBeInTheDocument();
    });

    describe("editing contacts", () => {
      beforeEach(() => {
        api.updateBusinessOwner.mockResolvedValue(test_system);
        api.updateProductOwner.mockResolvedValue(test_system);
        api.updateTechnicalOwner.mockResolvedValue(test_system);
        api.updateServiceOwner.mockResolvedValue(test_system);
        api.updateInformationAssetOwner.mockResolvedValue(test_system);
      });

      it("returns to system view on cancel", async () => {
        await checkCancelButton("update-contacts");
      });

      it("calls onChange callback when updates", async () => {
        renderWithRouting("123/update-contacts");
        const businessOwnerField = await screen.findByLabelText(
          /business owner/i
        );
        const productOwnerField = await screen.findByLabelText(
          /product owner/i
        );
        const technicalOwnerField = await screen.findByLabelText(
          /technical owner/i
        );
        const serviceOwnerField = await screen.findByLabelText(
          /service owner/i
        );
        const informationAssetOwnerField = await screen.findByLabelText(
          /information asset owner/i
        );
        const saveButton = screen.getByRole("button", { name: /save/i });

        overtype(businessOwnerField, "updated business owner");
        overtype(productOwnerField, "updated product owner");
        overtype(technicalOwnerField, "updated technical owner");
        overtype(serviceOwnerField, "updated service owner");
        overtype(informationAssetOwnerField, "updated information asset owner");
        user.click(saveButton);

        await waitFor(() => {
          expect(changeHandler).toBeCalled();
        });
      });

      it("returns to the system view after a successful update", async () => {
        const { history } = renderWithRouting("123/update-contacts");
        const businessOwnerField = await screen.findByLabelText(
          /business owner/i
        );
        const productOwnerField = await screen.findByLabelText(
          /product owner/i
        );
        const technicalOwnerField = await screen.findByLabelText(
          /technical owner/i
        );
        const serviceOwnerField = await screen.findByLabelText(
          /service owner/i
        );
        const informationAssetOwnerField = await screen.findByLabelText(
          /information asset owner/i
        );

        const saveButton = screen.getByRole("button", { name: /save/i });

        overtype(businessOwnerField, "updated business owner");
        overtype(productOwnerField, "updated product owner");
        overtype(technicalOwnerField, "updated technical owner");
        overtype(serviceOwnerField, "updated service owner");
        overtype(informationAssetOwnerField, "updated information asset owner");
        user.click(saveButton);

        await returnToSystemView(123, history);
        expect(api.updateBusinessOwner).toBeCalledWith(
          "123",
          expect.objectContaining({
            businessOwner: "updated business owner",
          })
        );
        expect(api.updateProductOwner).toBeCalledWith(
          "123",
          expect.objectContaining({
            productOwner: "updated product owner",
          })
        );
        expect(api.updateTechnicalOwner).toBeCalledWith(
          "123",
          expect.objectContaining({
            technicalOwner: "updated technical owner",
          })
        );
        expect(api.updateServiceOwner).toBeCalledWith(
          "123",
          expect.objectContaining({
            serviceOwner: "updated service owner",
          })
        );
        expect(api.updateInformationAssetOwner).toBeCalledWith(
          "123",
          expect.objectContaining({
            informationAssetOwner: "updated information asset owner",
          })
        );
        expect(
          await screen.findByText(/update has been saved/i)
        ).toBeInTheDocument();
      });

      it("does not send a request if field values are unchanged", async () => {
        const { history } = renderWithRouting("123/update-contacts");
        await screen.findByText("Test System");
        const saveButton = await screen.findByRole("button", { name: /save/i });

        user.click(saveButton);

        await returnToSystemView(123, history);
        expect(api.updateBusinessOwner).not.toBeCalled();
        expect(api.updateProductOwner).not.toBeCalled();
        expect(api.updateTechnicalOwner).not.toBeCalled();
        expect(api.updateServiceOwner).not.toBeCalled();
        expect(api.updateInformationAssetOwner).not.toBeCalled();
        expect(changeHandler).not.toBeCalled();
      });
    });

    describe("editing system info", () => {
      it("returns to system view on cancel", async () => {
        await checkCancelButton("update-info");
      });

      describe("system name", () => {
        it("returns to the system view after a successful update", async () => {
          api.updateSystemName.mockResolvedValue({
            ...test_system,
            name: "updated system name",
          });
          const { history } = renderWithRouting("123/update-info");
          const systemNameField = await screen.findByLabelText(/system name/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          user.clear(systemNameField);
          // noinspection ES6MissingAwait: there is no typing delay
          user.type(systemNameField, "updated system name");
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });
      });

      describe("system description", () => {
        it("returns to the system view after a successful update", async () => {
          api.updateSystemDescription.mockResolvedValue({
            ...test_system,
            description: "system description",
          });
          const { history } = renderWithRouting("123/update-info");
          const systemDescriptionField = await screen.findByLabelText(
            /system description/i
          );
          const saveButton = screen.getByRole("button", { name: /save/i });

          user.clear(systemDescriptionField);
          // noinspection ES6MissingAwait: there is no typing delay
          user.type(systemDescriptionField, "updated system description");
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(api.updateSystemDescription).toBeCalledWith(
            "123",
            expect.objectContaining({
              description: "updated system description",
            })
          );
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });

        it("does not send a request if field values are unchanged", async () => {
          const { history } = renderWithRouting("123/update-info");
          await screen.findByText("Test System");
          const saveButton = await screen.findByRole("button", {
            name: /save/i,
          });

          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(api.updateSystemDescription).not.toBeCalled();
          expect(changeHandler).not.toBeCalled();
        });
      });

      describe("system aliases", () => {
        it("returns to the system view after a successful update", async () => {
          api.updateSystemAliases.mockResolvedValue(test_system);
          const { history } = renderWithRouting("123/update-info");
          const systemAliasField = await screen.findByDisplayValue("");
          const saveButton = screen.getByRole("button", { name: /save/i });

          user.clear(systemAliasField);
          // noinspection ES6MissingAwait: there is no typing delay
          user.type(systemAliasField, "new alias");
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(api.updateSystemAliases).toBeCalledWith(
            "123",
            expect.objectContaining({
              aliases: ["new alias"],
            })
          );
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });

        it("does not send a request if field values are unchanged", async () => {
          const { history } = renderWithRouting("123/update-info");
          await screen.findByDisplayValue("");
          const saveButton = await screen.findByRole("button", {
            name: /save/i,
          });

          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(api.updateSystemAliases).not.toBeCalled();
          expect(changeHandler).not.toBeCalled();
        });
      });
    });

    describe("editing about", () => {
      it("returns to system view on cancel", async () => {
        await checkCancelButton("update-about");
      });

      describe("portfolio", () => {
        it("returns to the system view after a successful update", async () => {
          api.updatePortfolio.mockResolvedValue({
            ...test_system,
            portfolio: "updated portfolio",
          });
          const { history } = renderWithRouting("123/update-about");
          const radioButton = await screen.findByLabelText(
            /updated portfolio/i
          );
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.click(radioButton);
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });
      });

      describe("criticality", () => {
        it("returns to the system view after a successful update", async () => {
          api.updateCriticality.mockResolvedValue({
            ...test_system,
            criticality: "high",
          });
          const { history } = renderWithRouting("123/update-about");
          const radioButton = await screen.findByLabelText(/high/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.click(radioButton);
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(api.updateCriticality).toBeCalledWith(
            "123",
            expect.objectContaining({
              criticality: "high",
            })
          );
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });

        it("api is not called if criticality is unchanged", async () => {
          const { history } = renderWithRouting("123/update-about");
          const radioButton = await screen.findByLabelText(/low/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.click(radioButton);
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).not.toBeCalled();
          expect(api.updateCriticality).not.toBeCalled();
        });
      });

      describe("investment state", () => {
        it("returns to the system view after a successful update", async () => {
          api.updateInvestmentState.mockResolvedValue({
            ...test_system,
            investment_state: "sunset",
          });
          const { history } = renderWithRouting("123/update-about");
          const radioButton = await screen.findByDisplayValue(/sunset/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.click(radioButton);
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(api.updateInvestmentState).toBeCalledWith(
            "123",
            expect.objectContaining({
              investmentState: "sunset",
            })
          );
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });

        it("api is not called if investment state is unchanged", async () => {
          const { history } = renderWithRouting("123/update-about");
          const radioButton = await screen.findByDisplayValue(/invest/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.click(radioButton);
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).not.toBeCalled();
          expect(api.updateInvestmentState).not.toBeCalled();
        });
      });

      describe("developed by", () => {
        it("returns to the system view after a successful update", async () => {
          api.updateDevelopedBy.mockResolvedValue(test_system);
          const { history } = renderWithRouting("123/update-about");
          const textField = await screen.findByLabelText(/who develops/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          overtype(textField, "someone new");
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(api.updateDevelopedBy).toBeCalledWith(
            "123",
            expect.objectContaining({
              developedBy: "someone new",
            })
          );
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });

        it("api is not called if developed by is unchanged", async () => {
          const { history } = renderWithRouting("123/update-about");
          const saveButton = await screen.findByRole("button", {
            name: /save/i,
          });

          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).not.toBeCalled();
          expect(api.updateDevelopedBy).not.toBeCalled();
        });
      });

      describe("supported by", () => {
        it("returns to the system view after a successful update", async () => {
          api.updateSupportedBy.mockResolvedValue(test_system);
          const { history } = renderWithRouting("123/update-about");
          const textField = await screen.findByLabelText(/who supports/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          overtype(textField, "someone new");
          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).toBeCalled();
          expect(api.updateSupportedBy).toBeCalledWith(
            "123",
            expect.objectContaining({
              supportedBy: "someone new",
            })
          );
          expect(
            await screen.findByText(/update has been saved/i)
          ).toBeInTheDocument();
        });

        it("api is not called if supported by is unchanged", async () => {
          const { history } = renderWithRouting("123/update-about");
          const saveButton = await screen.findByRole("button", {
            name: /save/i,
          });

          user.click(saveButton);

          await returnToSystemView(123, history);
          expect(changeHandler).not.toBeCalled();
          expect(api.updateSupportedBy).not.toBeCalled();
        });
      });
    });

    describe("editing risks", () => {
      it("returns to system view on cancel", async () => {
        await checkCancelButton("update-risk?lens=lens1");
      });

      it("returns to the system view after a successful update", async () => {
        api.updateRisk.mockResolvedValue(test_system);
        renderWithRouting("123/update-risk?lens=lens1");
        const mediumRating = await screen.findByLabelText(/medium/i);
        const saveButton = screen.getByRole("button", { name: /save/i });

        user.click(mediumRating);
        user.click(saveButton);

        expect(
          await screen.findByText(/update has been saved/i)
        ).toBeInTheDocument();
        expect(changeHandler).toBeCalled();
        expect(api.updateRisk).toBeCalledWith(
          "123",
          expect.objectContaining({
            lens: "lens1",
            level: "medium",
          })
        );
      });

      it("does not invoke api if nothing changed", async () => {
        const { history } = renderWithRouting("123/update-risk?lens=lens2");
        const saveButton = await screen.findByRole("button", {
          name: /save/i,
        });

        user.click(saveButton);

        await returnToSystemView(123, history);
        expect(changeHandler).not.toBeCalled();
        expect(api.updateRisk).not.toBeCalled();
      });
    });

    describe("editing sunset", () => {
      it("returns to system view on cancel", async () => {
        await checkCancelButton("update-key-dates");
      });

      it("returns to the system view after a successful update", async () => {
        api.updateSunset.mockResolvedValue(test_system);
        renderWithRouting("123/update-key-dates");

        const sunsetDay = await screen.findByLabelText(/day/i);
        const sunsetMonth = await screen.findByLabelText(/month/i);
        const sunsetYear = await screen.findByLabelText(/year/i);
        const additionalSunsetInformation = await screen.findByLabelText(
          /additional information/i
        );
        const saveButton = screen.getByRole("button", { name: /save/i });

        overtype(sunsetDay, "5");
        overtype(sunsetMonth, "7");
        overtype(sunsetYear, "2021");
        overtype(additionalSunsetInformation, "some info");
        user.click(saveButton);

        expect(
          await screen.findByText(/update has been saved/i)
        ).toBeInTheDocument();
        expect(changeHandler).toBeCalled();
        expect(api.updateSunset).toBeCalledWith(
          "123",
          expect.objectContaining({
            sunset: {
              date: DateTime.local(2021, 7, 5),
              additionalInformation: "some info",
            },
          })
        );
      });

      it("does not invoke api if nothing changed", async () => {
        const { history } = renderWithRouting("123/update-key-dates");
        const saveButton = await screen.findByRole("button", {
          name: /save/i,
        });

        user.click(saveButton);

        await returnToSystemView(123, history);
        expect(changeHandler).not.toBeCalled();
        expect(api.updateSunset).not.toBeCalled();
      });
    });
  });
});

function overtype(field, value) {
  user.clear(field);
  // noinspection JSIgnoredPromiseFromCall
  user.type(field, value);
}

async function checkCancelButton(path) {
  const { history } = renderWithRouting(`123/${path}`);
  const cancelButton = await screen.findByRole("button", { name: /cancel/i });

  user.click(cancelButton);

  await returnToSystemView(123, history);
  forEach(omit(api, "getSystem"), (method) => {
    expect(method).not.toBeCalled();
  });
}

async function returnToSystemView(systemId, history) {
  await waitFor(() => {
    expect(history).toHaveProperty("index", 1);
    expect(history).toHaveProperty("location.pathname", `/system/${systemId}`);
  });
}

function renderWithRouting(path, renderOptions) {
  const history = createMemoryHistory({ initialEntries: [`/system/${path}`] });
  const renderResult = render(
    <Router history={history}>
      <Route path="/system/:id">
        <System
          portfolios={["original portfolio", "updated portfolio"]}
          onBeforeNameChange={() => false}
          onChange={changeHandler}
        />
      </Route>
    </Router>,
    renderOptions
  );
  return { ...renderResult, history };
}
