import api from '../api'
import data from '../../data/systems_dummy.json';
import { rest } from "msw";
import { setupServer } from "msw/node";
import System from "../../components/System/System";

const server = setupServer(
    rest.get("/api/systems", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json(data)
        );
    })
);


describe("api", () => {
    beforeAll(() => server.listen({onUnhandledRequest: "error"}));

    afterAll(() => server.close());

    afterEach(() => server.resetHandlers());

    it('returns a list of systems sorted by name', async () => {
        const listOfSystems =  api.getAllSystems();
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
});



