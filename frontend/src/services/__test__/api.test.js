import { rest } from "msw";
import { setupServer } from "msw/node";

import api from '../api'
import data from '../../data/systems_dummy.json';

const server = setupServer(
  rest.get("/api/systems", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json(data)
    );
  })
);

describe("api", () => {
  beforeAll(() => server.listen({ onUnhandledRequest: "error" }));

  afterAll(() => server.close());

  afterEach(() => server.resetHandlers());

  it('returns a list of systems sorted by name', async () => {
    const listOfSystems = api.getAllSystems();
    await expect(listOfSystems).resolves.toMatchObject({
      systems: [
        expect.objectContaining({
          name: "Our second system"
        }),
        expect.objectContaining({
          name: "System Register"
        })
      ]
    });
  })

  it('returns the system with the specified ID', async () => {
    const system = api.getSystem('1');
    await expect(system).resolves.toMatchObject({
      id: 1,
      name: "System Register"
    });
  })

  describe("update contacts", () => {
    it("sends changed contacts to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-product-owner", (req, res, ctx) => {
          const { product_owner: productOwner } = req.body;
          if (productOwner !== "old owner") {
            console.error("New product owner does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateContacts(345, {
        productOwner: "old owner"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });
  });
});
