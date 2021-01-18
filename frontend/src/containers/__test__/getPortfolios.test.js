import getPortfolios from '../getPortfolios';

describe('getPortfolios', () => {
  it.each([null, [], undefined])
  ('returns an empty array when no systems are available: %p', (systems) => {
    const portfolios = getPortfolios(systems);

    expect(portfolios).toStrictEqual([]);
  });

  it('returns a list of unique portfolios derived from passed in systems', () => {
    const systems = [
      { system: "Sys-A", portfolio: "PF-1" },
      { system: "Sys-B", portfolio: "PF-2" },
      { system: "Sys-C", portfolio: "PF-2" },
      { system: "Sys-D", portfolio: "PF-3" },
      { system: "Sys-E", portfolio: "PF-3" },
      { system: "Sys-F", portfolio: "PF-4" },
      { system: "Sys-G", portfolio: "PF-4" }
    ];

    const portfolios = getPortfolios(systems);

    expect(portfolios).toEqual(["PF-1", "PF-2", "PF-3", "PF-4"]);
  });

  it("removes excess whitespace", () => {
    const systems = [{ portfolio: "   PF-1" }, { portfolio: " PF-2  " }];

    const portfolios = getPortfolios(systems);

    expect(portfolios).toEqual(["PF-1", "PF-2"]);
  });

  it('removes empty, null and undefined portfolio values for individual systems', () => {
    const systems = [
      { system: "Sys-A", portfolio: "PF-1" },
      { system: "Sys-B" },
      { system: "Sys-C", portfolio: "" },
      { system: "Sys-D", portfolio: null }
    ];

    const portfolios = getPortfolios(systems);

    expect(portfolios).toEqual(["PF-1"]);
  });

  it('removes "Unknown" portfolio values to avoid duplicate radio button creation', () => {
    const systems = [
      { system: "Sys-A", portfolio: "PF-1" },
      { system: "Sys-C", portfolio: "Unknown" },
      { system: "Sys-D", portfolio: "Unknown" }
    ];

    const portfolios = getPortfolios(systems);

    expect(portfolios).toEqual(["PF-1"]);
  });

  it("sorts portfolios alphabetically", () => {
    const systems = [
      { portfolio: "PF-1" },
      { portfolio: "AX-9" },
      { portfolio: "CR-2" }
    ];

    const portfolios = getPortfolios(systems);

    expect(portfolios).toEqual(["AX-9", "CR-2", "PF-1"]);
  });
});
