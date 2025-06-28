import { Dropdown, DropdownHeader, DropdownItem, Navbar, NavbarBrand } from "flowbite-react";
import { Bell } from "lucide-react";

export default function Topbar({ pageTitle }: { pageTitle: string }) {
    return (
        <Navbar fluid rounded className="border-b-gray-600 shadow-sm px-4 bg-white">
            <NavbarBrand href="#">
                <span className="self-center whitespace-nowrap text-xl font-semibold text-gray-800">
                    {pageTitle}
                </span>
            </NavbarBrand>

            <div className="flex items-center gap-4">
                <button type="button" className="relative text-gray-600 hover:text-blue-600 focus:outline-none">
                    <Bell className="w-6 h-6" />
                    <span className="absolute -top-1 -right-1 w-4 h-4 text-[10px] font-bold text-white bg-red-600 rounded-full">
                        3
                    </span>
                </button>

                <Dropdown
                    inline
                    label={
                        <img
                            src="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
                            className="w-10 h-10 rounded-full"
                            alt="User avatar"
                        />
                    }
                >
                    <DropdownHeader>
                        <span className="block text-sm">Admin</span>
                        <span className="block truncate text-sm font-medium">admin@email.com</span>
                    </DropdownHeader>
                    <DropdownItem>Dashboard</DropdownItem>
                    <DropdownItem>Settings</DropdownItem>
                    <DropdownItem>Logout</DropdownItem>
                </Dropdown>
            </div>
        </Navbar>
    );
}
