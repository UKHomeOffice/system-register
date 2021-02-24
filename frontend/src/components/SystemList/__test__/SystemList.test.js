import React from "react";
import renderer from "react-test-renderer";
import user from "@testing-library/user-event";
import { render as _render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

import SystemList from "../SystemList";

const test_data = {
  systems: [
    {
      name: "System 1",
      portfolio: "Portfolio 1",
      aliases: ["Alias A"],
      last_updated: {},
    },
    {
      name: "System 3",
      portfolio: "Portfolio 2",
      aliases: ["Alias B", "Alias C"],
      last_updated: {},
    },
    {
      name: "System 2",
      portfolio: "Portfolio 2",
      aliases: [],
      last_updated: {},
    },
  ],
};

describe("SystemList", () => {
  describe("searching by name", () => {
    it("renders search, filter and all systems when loaded", () => {
      render(<SystemList register={test_data} />);

      expect(screen.getByLabelText(/find a system/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/filter by/i)).toBeInTheDocument();
      expectSystems("System 1", "System 2", "System 3");
    });

    it("finds no systems when nothing matches the search string", async () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("System 99");

      noSystemsShown();
    });

    it("shows all systems when search string removed", () => {
      render(<SystemList register={test_data} />);
      whenUserSearchesFor("System 1");
      expectSystems("System 1");

      whenUserSearchesFor("");

      expectSystems("System 1", "System 2", "System 3");
    });

    it("finds a system which fully match search string", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("System 1");

      expectSystems("System 1");
    });

    it("finds a system which partially matches search string", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("ystem 1");

      expectSystems("System 1");
    });

    it("finds multiple systems which match search string", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("System");

      expectSystems("System 1", "System 2", "System 3");
    });

    it("ignores case when searching", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("system 2");

      expectSystems("System 2");
    });
  });

  describe("searching by alias", () => {
    it("finds a system who’s alias fully matches the search string", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("Alias A");

      expectSystems("System 1");
    });

    it("finds all system who’s alias partially matches the search string", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("Alias");

      expectSystems("System 1", "System 3");
    });

    it("finds no system when no aliases match the search string", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("Alias Z");

      noSystemsShown();
    });

    it("finds a system who’s alias in lowercase matches the search string", () => {
      render(<SystemList register={test_data} />);

      whenUserSearchesFor("alias a");

      expectSystems("System 1");
    });
  });

  describe("filtering by Portfolio", () => {
    it("filters systems when one system matches", () => {
      render(<SystemList register={test_data} />);

      whenUserSelectsPortfolio("Portfolio 1");

      expectSystems("System 1");
    });

    it("filters systems when multiple systems match", () => {
      render(<SystemList register={test_data} />);

      whenUserSelectsPortfolio("Portfolio 2");

      expectSystems("System 2", "System 3");
    });
  });

  describe("portfolio filter and search terms combined", () => {
    it("correctly combines filters to return a system", () => {
      render(<SystemList register={test_data} />);

      whenUserSelectsPortfolio("Portfolio 2");
      whenUserSearchesFor("3");

      expectSystems("System 3");
    });

    it("correctly combines filters to return no systems", () => {
      render(<SystemList register={test_data} />);

      whenUserSelectsPortfolio("Portfolio 2");
      whenUserSearchesFor("1");

      noSystemsShown();
    });
  });

  it("matches snapshot", () => {
    expect(
      renderer.create(
        <MemoryRouter>
          <SystemList register={test_data} />
        </MemoryRouter>
      )
    ).toMatchSnapshot();
  });
});

function render(component) {
  return _render(component, { wrapper: MemoryRouter });
}

//dsl
const isSystem = (_, element) => element.classList.contains("systemCard");

const noSystemsShown = () => expectSystems();

function expectSystems(...systemNames) {
  const systems = screen.queryAllByRole(isSystem);
  expect(systems).toHaveLength(systemNames.length);

  for (const systemName of systemNames) {
    expect(
      screen.getByText(systemName, { selector: ".systemCard *" })
    ).toBeInTheDocument();
  }
}

function whenUserSearchesFor(value) {
  const searchField = screen.getByRole("textbox");
  user.clear(searchField);
  // noinspection JSIgnoredPromiseFromCall
  user.type(searchField, value);
  user.tab();
}

function whenUserSelectsPortfolio(portfolio) {
  const portfolioList = screen.getByRole("combobox");
  user.selectOptions(portfolioList, portfolio);
}
