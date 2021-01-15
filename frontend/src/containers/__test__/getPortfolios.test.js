import getPortfolios from '../getPortfolios';

describe('getPortfolios', () => {
  it.each([null, [], undefined])
  ('returns an empty array when no systems are available: %p', (systems) => {
    const portfolios = getPortfolios(systems);

    expect(portfolios).toStrictEqual([]);
  })

  it('returns an array with one value when an array with one value is passed in', () => {
    const systems = [{portfolio: "example portfolio"}];
    const portfolios = getPortfolios(systems);

    expect(portfolios).toEqual(["example portfolio"]);
  })


})