import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Root, {loader as rootLoader, action as rootAction} from "./routes/root.jsx";
import ErrorPage from "./error-page.jsx";
import Contact, {
    loader as contactLoader
} from "./routes/contact.jsx";

const router = createBrowserRouter([{
    path: "/",
    element: <Root/>,
    errorElement: <ErrorPage/>,
    loader: rootLoader,
    action: rootAction,
    children: [
        {
            path: "contacts/:contactId",
            element: <Contact/>,
            loader: contactLoader
        }
    ]
},

])

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>,
)
